package cz.osu.theatre.services;

import cz.osu.theatre.errors.exceptions.AuthorAlreadyExistException;
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
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final TheatreActivityRepository theatreActivityRepository;

    @Override
    public Set<Author> checkAuthors(Set<Author> authors) {
        Set<Author> authorsSet = new HashSet<>();

        authors.forEach(author -> this.authorRepository.findByFirstNameAndLastName(author.getFirstName(),author.getLastName())
                .ifPresentOrElse(authorsSet::add,() -> {
                    Author newAuthor = new Author(author.getFirstName(),author.getLastName());
                    authorsSet.add(this.authorRepository.save(newAuthor));
                }));

        return authorsSet;
    }

    @Override
    public void createAuthor(String firstName,String lastName) {
        authorValidation(firstName, lastName);

        if(this.authorRepository.findByFirstNameAndLastName(firstName,lastName).isPresent()){
            throw new AuthorAlreadyExistException(String.format("Could not create a author with firstname: %s and lastname: %s due to duplication",firstName,lastName));
        }

        Author author = new Author(firstName,lastName);
        this.authorRepository.save(author);
    }

    @Override
    public void updateAuthor(long idAuthor,String firstName,String lastName) {
        authorValidation(firstName, lastName);

        Author author = this.authorRepository.findById(idAuthor)
                .orElseThrow(() -> new AuthorNotFoundException(String.format("Could not find a author with fistName: %s and lastName: %s", firstName,lastName)));

        if(this.authorRepository.findByFirstNameAndLastName(firstName,lastName).isPresent()){
            throw new AuthorAlreadyExistException(String.format("Could not update author with id: %d, firstname: %s, lastName: %s due to duplication",idAuthor,firstName,lastName));
        }

        author.setFirstName(firstName);
        author.setLastName(lastName);
        this.authorRepository.save(author);
    }

    @Override
    public List<Author> getAuthorsByName(String firstName,String lastName, Pageable pageable) {
        if ((firstName == null || firstName.trim().isEmpty()) && (lastName == null || lastName.trim().isEmpty()))
            throw new ParameterException("Parameters cannot be null or empty!");

        if(lastName == null){
            return this.authorRepository.findAuthorsByFirstName(firstName,pageable);
        }else if(firstName == null){
            return this.authorRepository.findAuthorsByLastName(lastName,pageable);
        }else{
            return this.authorRepository.findAuthorsByFirstNameAndLastName(firstName, lastName, pageable);
        }
    }

    @Override
    public List<Author> getAuthorsByTheatreId(long idTheatre) {
        TheatreActivity theatreActivity = theatreActivityRepository.findById(idTheatre)
                .orElseThrow(() -> new TheatreActivityNotFoundException(String.format("Could not find a activity with id: %d", idTheatre)));

        return theatreActivity.getAuthors().stream().toList();
    }

    private void authorValidation(String firstName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty())
            throw new ParameterException("FirstName for Author cannot be null or empty!");
        if (lastName == null || lastName.trim().isEmpty())
            throw new ParameterException("LastName for Author cannot be null or empty!");
    }

}
