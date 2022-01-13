package cz.osu.theatre.services;

import cz.osu.theatre.errors.exceptions.*;
import cz.osu.theatre.models.entities.AppUser;
import cz.osu.theatre.models.entities.Rating;
import cz.osu.theatre.models.entities.TheatreActivity;
import cz.osu.theatre.repositories.AppUserRepository;
import cz.osu.theatre.repositories.RatingRepository;
import cz.osu.theatre.repositories.TheatreActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final TheatreActivityRepository theatreActivityRepository;
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;

    @Override
    public void createRating(int ratingValue, long idTheatreActivity) {
        AppUser currentUser = this.appUserService.getCurrentUser();

        TheatreActivity theatreActivity = this.theatreActivityRepository.findById(idTheatreActivity)
                .orElseThrow(() -> new TheatreActivityNotFoundException(String.format("Could not find a theatre activity with id: %d", idTheatreActivity)));

        if (ratingValue > 5 || ratingValue < 0)
            throw new RatingValueException(String.format("Rating value must be between 5 or 0! Current value: %d", ratingValue));

        this.ratingRepository.findByTheatreActivityIdAndRatingCreatorId(theatreActivity.getId(), currentUser.getId())
                .ifPresent(rating -> {
                    throw new RatingAlreadyExistsException(String.format("User with id: %d already rated theatre activity with id: %d", currentUser.getId(), theatreActivity.getId()));
                });

        Rating rating = new Rating(ratingValue, currentUser, theatreActivity);
        this.ratingRepository.save(rating);

        this.calculateRatingForActivity(theatreActivity);
    }

    @Override
    public void updateRating(int ratingValue, long idTheatreActivity) {
        AppUser currentUser = this.appUserService.getCurrentUser();

        TheatreActivity theatreActivity = this.theatreActivityRepository.findById(idTheatreActivity)
                .orElseThrow(() -> new TheatreActivityNotFoundException(String.format("Could not find a theatre activity with id: %d", idTheatreActivity)));

        if (ratingValue > 5 || ratingValue < 0)
            throw new RatingValueException(String.format("Rating value must be between 5 or 0! Current value: %d", ratingValue));

        Rating userRating = this.ratingRepository.findByTheatreActivityIdAndRatingCreatorId(theatreActivity.getId(), currentUser.getId())
                .orElseThrow(() -> new RatingNotExistsException(String.format("Could not find rating for theatre activity with id: %d and user with id: %d", theatreActivity.getId(), currentUser.getId())));

        userRating.setRatingValue(ratingValue);
        this.ratingRepository.save(userRating);

        this.calculateRatingForActivity(theatreActivity);
    }

    @Override
    public void deleteRatingOfUser(long idTheatreActivity) {
        AppUser currentUser = this.appUserService.getCurrentUser();

        TheatreActivity theatreActivity = this.theatreActivityRepository.findById(idTheatreActivity)
                .orElseThrow(() -> new TheatreActivityNotFoundException(String.format("Could not find a theatre activity with id: %d", idTheatreActivity)));

        Rating userRating = this.ratingRepository.findByTheatreActivityIdAndRatingCreatorId(theatreActivity.getId(), currentUser.getId())
                .orElseThrow(() -> new RatingNotExistsException(String.format("Could not find rating for theatre activity with id: %d and user id: %d", theatreActivity.getId(), currentUser.getId())));

        this.ratingRepository.delete(userRating);

        this.calculateRatingForActivity(theatreActivity);
    }

    @Override
    public List<Rating> getAllRatingForActivityId(long idTheatreActivity) {
        TheatreActivity theatreActivity = this.theatreActivityRepository.findById(idTheatreActivity)
                .orElseThrow(() -> new TheatreActivityNotFoundException(String.format("Could not find a theatre activity with id: %d", idTheatreActivity)));

        return this.ratingRepository.findAllByTheatreActivityId(theatreActivity.getId());
    }

    @Override
    public List<Rating> getAllRatingsOfUser(long idUser) {
        AppUser user = this.appUserRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException(String.format("Could not find a user with id: %d", idUser)));

        return this.ratingRepository.findAllByRatingCreatorId(user.getId());
    }

    @Override
    public Rating getRatingByTheatreIdAndRatingId(long idTheatreActivity, long idRating) {
        return this.ratingRepository.findByIdAndTheatreActivityId(idRating, idTheatreActivity)
                .orElseThrow(() -> new RatingNotExistsException(String.format("Could not find a rating with id: %d for activity with id: %d", idRating, idTheatreActivity)));
    }

    @Override
    public void calculateRatingForActivity(TheatreActivity theatreActivity) {
        if (theatreActivity == null) throw new NullPointerException("Parameter theaterActivity cannot be null!");

        double ratingValue = 0;

        double numberOfRatings = theatreActivity.getRatings().size();

        if (numberOfRatings > 0) {
            double totalRatingValue = theatreActivity.getRatings().stream().mapToInt(Rating::getRatingValue).sum();
            ratingValue = totalRatingValue / numberOfRatings;
        }

        theatreActivity.setRating(ratingValue);
        this.theatreActivityRepository.save(theatreActivity);
    }
}
