package cz.osu.theatre.repositories;

import cz.osu.theatre.models.entities.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    Optional<Author> findByFirstNameAndLastName(String firstName,String lastName);

    @Query("SELECT a FROM author a WHERE a.firstName LIKE %:firstName%")
    List<Author> findAuthorsByFirstName(@Param("firstName") String firstName, Pageable pageable);

    @Query("SELECT a FROM author a WHERE a.lastName LIKE %:lastName%")
    List<Author> findAuthorsByLastName(@Param("lastName") String lastName, Pageable pageable);

    @Query("SELECT a FROM author a WHERE a.firstName LIKE %:firstName% AND a.lastName LIKE %:lastName%")
    List<Author> findAuthorsByFirstNameAndLastName(@Param("firstName") String firstName,@Param("lastName") String lastName, Pageable pageable);
}
