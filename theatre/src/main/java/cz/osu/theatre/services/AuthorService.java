package cz.osu.theatre.services;

import cz.osu.theatre.models.entities.Author;
import cz.osu.theatre.models.entities.TheatreActivity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface AuthorService {
    Set<Author> checkAuthors(Set<Author> authors);

    void createAuthor(String firstName,String lastName);

    void updateAuthor(long idAuthor,String firstName,String lastName);

    List<Author> getAuthorsByName(String firstName,String lastName, Pageable pageable);

    List<Author> getAuthorsByTheatreId(long idTheatre);
}
