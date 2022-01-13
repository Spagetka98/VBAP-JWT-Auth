package cz.osu.theatre.controllers;

import cz.osu.theatre.models.entities.Rating;
import cz.osu.theatre.models.requests.RatingCreationRequest;
import cz.osu.theatre.models.requests.RatingDeleteRequest;
import cz.osu.theatre.models.requests.RatingUpdateRequest;
import cz.osu.theatre.models.responses.RatingResponse;
import cz.osu.theatre.services.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("/getRatingByTheatreId/{idTheatre}/ratingId/{idRating}")
    public ResponseEntity<?> getRatingByTheatreIdAndRatingId(@PathVariable long idTheatre,@PathVariable long idRating){
        log.info(String.format("Loading all ratings for theatre activity with id: %d", idRating));

        Rating rating = this.ratingService.getRatingByTheatreIdAndRatingId(idTheatre,idRating);

        log.info("Ratings was successfully loaded!");

        return ResponseEntity.ok(new RatingResponse(rating.getId(),rating.getRatingValue(),rating.getRatingCreator().getUsername(),rating.getRatingCreator().getEmail()));
    }

    @GetMapping("/getAllRatingsForActivity/{idTheatre}")
    public ResponseEntity<?> getAllRatingsForActivity(@PathVariable long idTheatre){
        log.info(String.format("Loading all ratings for theatre activity with id: %d", idTheatre));

        List<Rating> ratings = this.ratingService.getAllRatingForActivityId(idTheatre);

        log.info("Ratings was successfully loaded!");

        return ResponseEntity.ok(ratings.stream().map(rating -> new RatingResponse(rating.getId(),rating.getRatingValue(),rating.getRatingCreator().getUsername(),rating.getRatingCreator().getEmail())).toList());
    }

    @GetMapping("/getAllRatingsOfUser/{idUser}")
    public ResponseEntity<?> getAllRatingsOfUser(@PathVariable long idUser){
        log.info(String.format("Loading all ratings for user with id: %d", idUser));

        List<Rating> ratings = this.ratingService.getAllRatingsOfUser(idUser);

        log.info("Ratings was successfully loaded!");

        return ResponseEntity.ok(ratings.stream().map(rating -> new RatingResponse(rating.getId(),rating.getRatingValue(),rating.getRatingCreator().getUsername(),rating.getRatingCreator().getEmail())).toList());
    }

    @PostMapping("/createRating")
    public ResponseEntity<?> createRating(@Valid @RequestBody RatingCreationRequest ratingCreationRequest){
        log.info(String.format("Creating rating for theatre activity with id: %d and value: %d", ratingCreationRequest.getRating(), ratingCreationRequest.getIdActivity()));

        this.ratingService.createRating(ratingCreationRequest.getRating(), ratingCreationRequest.getIdActivity());

        log.info("Rating was successfully created!");

        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateRatingOfUser")
    public ResponseEntity<?> updateRating(@Valid @RequestBody RatingUpdateRequest ratingUpdateRequest){
        log.info(String.format("Updating rating for theatre activity with id: %d and value: %d ", ratingUpdateRequest.getIdActivity(),ratingUpdateRequest.getRating()));

        this.ratingService.updateRating(ratingUpdateRequest.getRating(),ratingUpdateRequest.getIdActivity());

        log.info("Rating was successfully updated!");

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteRatingOfUser")
    public ResponseEntity<?> deleteRating(@RequestBody RatingDeleteRequest ratingDeleteRequest){
        log.info(String.format("Deleting rating for theatre activity with id: %d", ratingDeleteRequest.getIdActivity()));

        this.ratingService.deleteRatingOfUser(ratingDeleteRequest.getIdActivity());

        log.info("Rating was successfully deleted!");

        return ResponseEntity.ok().build();
    }
}
