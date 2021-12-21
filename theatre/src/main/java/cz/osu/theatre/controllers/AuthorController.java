package cz.osu.theatre.controllers;

import cz.osu.theatre.models.entities.Author;
import cz.osu.theatre.models.requests.AuthorCreationRequest;
import cz.osu.theatre.models.requests.AuthorUpdateRequest;
import cz.osu.theatre.models.responses.AuthorResponse;
import cz.osu.theatre.services.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/getAuthorsByName")
    public ResponseEntity<?> getAuthorsByName(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int authorPerPage
    ){
        log.info(String.format("Searching for authors with firstName: %s and lastName: %s",firstName,lastName));

        List<Author> authors = this.authorService.getAuthorsByName(firstName,lastName, PageRequest.of(page,authorPerPage));

        log.info("End of search");

        return ResponseEntity.ok(authors.stream().map((author) -> new AuthorResponse(author.getId(),author.getFirstName(),author.getLastName())));
    }

    @GetMapping("/getAuthorsByTheatreId/{idTheatre}")
    public ResponseEntity<?> getAuthorsByTheatreId(@PathVariable long idTheatre){
        log.info(String.format("Loading authors for theatre id: %d",idTheatre));

        List<Author> authors = this.authorService.getAuthorsByTheatreId(idTheatre);

        log.info("Loading of authors was successful");

        return ResponseEntity.ok(authors.stream().map((author -> new AuthorResponse(author.getId(),author.getFirstName(),author.getLastName()))));
    }

    @PostMapping("/createAuthor")
    public ResponseEntity<?> createAuthor(@Valid @RequestBody AuthorCreationRequest authorCreationRequest){
        log.info(String.format("Creating author with firstName: %s and lastName: %s", authorCreationRequest.getFirstName(), authorCreationRequest.getLastName()));

        this.authorService.createAuthor(authorCreationRequest.getFirstName(), authorCreationRequest.getLastName());

        log.info("Author was successfully created!");

        return ResponseEntity.ok().build();

    }

    @PutMapping("/updateAuthor")
    public ResponseEntity<?> updateAuthor(@Valid @RequestBody AuthorUpdateRequest authorUpdateRequest){
        log.info(String.format("Updating author with id: %s, firstName: %s and lastName: %s",authorUpdateRequest.getIdAuthor(), authorUpdateRequest.getFirstName(), authorUpdateRequest.getLastName()));

        this.authorService.updateAuthor(authorUpdateRequest.getIdAuthor(),authorUpdateRequest.getFirstName(), authorUpdateRequest.getLastName());

        log.info("Author was successfully updated!");

        return ResponseEntity.ok().build();

    }
}
