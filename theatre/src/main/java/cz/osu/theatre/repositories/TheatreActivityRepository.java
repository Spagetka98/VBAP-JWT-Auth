package cz.osu.theatre.repositories;

import cz.osu.theatre.models.entities.TheatreActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheatreActivityRepository extends JpaRepository<TheatreActivity,Long> {
    List<TheatreActivity> findByAuthors_id(long idAuthor, Pageable pageable);

    List<TheatreActivity> findByDivisions_idIn(List<Long> ids,Pageable pageable);

    List<TheatreActivity> findTop5ByRatingGreaterThanOrderByRatingDesc(double rating);

    Page<TheatreActivity> findAll(Pageable pageable);
}
