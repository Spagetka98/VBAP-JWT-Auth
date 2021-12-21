package cz.osu.theatre.repositories;

import cz.osu.theatre.models.entities.TheatreActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TheatreActivityRepository extends JpaRepository<TheatreActivity,Long> {
    List<TheatreActivity> findByAuthors_id(long idAuthor, Pageable pageable);

    List<TheatreActivity> findByDivisions_idIn(List<Long> ids,Pageable pageable);

    List<TheatreActivity> findTop5ByRatingGreaterThanOrderByRatingDesc(double rating);

    Page<TheatreActivity> findAll(Pageable pageable);

    Optional<TheatreActivity> findByNdmId(long ndmId);

    Optional<TheatreActivity> findByNameAndStageAndDateAndStartAndEnd(String name, String stage, LocalDate date,String start,String end);
}
