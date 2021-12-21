package cz.osu.theatre.repositories;

import cz.osu.theatre.models.entities.Division;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DivisionRepository extends JpaRepository<Division,Long> {
    Optional<Division> findByName(String name);

}
