package cz.osu.theatre.repositories;

import cz.osu.theatre.models.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByTheatreActivityIdAndRatingCreatorId(long idTheatre, long idRatingOwner);

    List<Rating> findAllByTheatreActivityId(long idTheatre);

    List<Rating> findAllByRatingCreatorId(long idUser);

    Optional<Rating> findByIdAndTheatreActivityId(long idRating, long idTheatre);
}
