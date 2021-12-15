package cz.osu.theatre.repositories;

import cz.osu.theatre.models.entities.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    Optional<Author> findByName(String name);

    @Query("SELECT a FROM author a WHERE a.name LIKE %:name%")
    List<Author> findAuthorsByName(@Param("name") String name, Pageable pageable);
}
