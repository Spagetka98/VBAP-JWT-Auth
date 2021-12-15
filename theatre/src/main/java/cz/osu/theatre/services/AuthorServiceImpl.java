package cz.osu.theatre.services;

import cz.osu.theatre.errors.exceptions.AuthorNotFoundException;
import cz.osu.theatre.errors.exceptions.ParameterException;
import cz.osu.theatre.errors.exceptions.TheatreActivityNotFoundException;
import cz.osu.theatre.models.entities.Author;
import cz.osu.theatre.models.entities.TheatreActivity;
import cz.osu.theatre.repositories.AuthorRepository;
import cz.osu.theatre.repositories.TheatreActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;
    private final TheatreActivityRepository theatreActivityRepository;

    @Override
    public Set<Author> checkAuthors(Set<Author> authors) {
        Set<Author> authorsSet = new HashSet<>();

        authors.forEach(author -> {
            this.authorRepository.findByName(author.getName())
                    .ifPresentOrElse(authorsSet::add,() -> {
                        Author newAuthor = new Author(author.getName());
                        authorsSet.add(this.authorRepository.save(newAuthor));
                    });
        });

        return authorsSet;
    }

    @Override
    public void createAuthor(String authorName) {
        if(authorName == null || authorName.trim().isEmpty()) throw new ParameterException("Name for Author cannot be null or empty!");

        Author author = new Author(authorName);
        this.authorRepository.save(author);
    }

    @Override
    public void updateAuthor(String authorName) {
        if(authorName == null || authorName.trim().isEmpty()) throw new ParameterException("Name for Author cannot be null or empty!");

        Author author = this.authorRepository.findByName(authorName)
                .orElseThrow(() -> new AuthorNotFoundException(String.format("Could not find a author with name: %s",authorName)));

        author.setName(authorName);
        this.authorRepository.save(author);
    }

    @Override
    public List<Author> getAuthorsByName(String authorName, Pageable pageable) {
        if(authorName == null || authorName.trim().isEmpty()) throw new ParameterException("Name for Author cannot be null or empty!");

        return this.authorRepository.findAuthorsByName(authorName,pageable);
    }

    @Override
    public List<Author> getAuthorsByTheatreId(long idTheatre) {
        TheatreActivity theatreActivity = theatreActivityRepository.findById(idTheatre)
                .orElseThrow(() -> new TheatreActivityNotFoundException(String.format("Could not find a activity with id: %d",idTheatre)));

        return theatreActivity.getAuthors().stream().toList();
    }
}
