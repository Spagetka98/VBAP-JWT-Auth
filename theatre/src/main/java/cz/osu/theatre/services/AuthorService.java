package cz.osu.theatre.services;

import cz.osu.theatre.models.entities.Author;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface AuthorService {
    Set<Author> checkAuthors(Set<Author> authors);

    void createAuthor(String authorName);

    void updateAuthor(String authorName);

    List<Author> getAuthorsByName(String authorName, Pageable pageable);

    List<Author> getAuthorsByTheatreId(long idTheatre);
}
