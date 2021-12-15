package cz.osu.theatre.services;

import cz.osu.theatre.models.entities.Rating;
import cz.osu.theatre.models.entities.TheatreActivity;

import java.util.List;

public interface RatingService {
    void calculateRatingForActivity(TheatreActivity theatreActivity);

    void createRating(int ratingValue,long idTheatreActivity);

    void updateRating(int ratingValue, long idRating,long idTheatreActivity);

    void deleteRating(long idRating,long idTheatreActivity);

    List<Rating> getAllRatingForActivityId(long idTheatreActivity);

    List<Rating> getAllRatingsOfUser(long idUser);

    Rating getRatingByTheatreIdAndRatingId(long idTheatreActivity, long idRating);
}
