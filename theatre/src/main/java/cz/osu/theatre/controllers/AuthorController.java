package cz.osu.theatre.controllers;

import cz.osu.theatre.models.entities.Author;
import cz.osu.theatre.models.entities.Division;
import cz.osu.theatre.models.requests.AuthorCreationRequest;
import cz.osu.theatre.models.responses.AuthorResponse;
import cz.osu.theatre.models.responses.DivisionResponse;
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
@RequestMapping("/theatreActivity")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/getAuthorsByName")
    public ResponseEntity<?> getAuthorsByName(
            @RequestParam String authorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int authorPerPage
    ){
        log.info(String.format("Searching for authors with name: %s",authorName));

        List<Author> authors = this.authorService.getAuthorsByName(authorName, PageRequest.of(page,authorPerPage));

        log.info("End of search");

        return ResponseEntity.ok(authors.stream().map((author) -> new AuthorResponse(author.getId(),author.getName())));
    }

    @GetMapping("/getAuthorsByTheatreId/{idTheatre}")
    public ResponseEntity<?> getAuthorsByTheatreId(@PathVariable long idTheatre){
        log.info(String.format("Loading authors for theatre id: %d",idTheatre));

        List<Author> authors = this.authorService.getAuthorsByTheatreId(idTheatre);

        log.info("Loading of authors was successful");

        return ResponseEntity.ok(authors.stream().map((author -> new AuthorResponse(author.getId(),author.getName()))));
    }

    @PostMapping("/createAuthor")
    public ResponseEntity<?> createAuthor(@Valid @RequestBody AuthorCreationRequest authorCreationRequest){
        log.info(String.format("Creating author with name: %s",authorCreationRequest.getAuthorName()));

        this.authorService.createAuthor(authorCreationRequest.getAuthorName());

        log.info("Author was successfully created!");

        return ResponseEntity.ok().build();

    }

    @PutMapping("/updateAuthor")
    public ResponseEntity<?> updateAuthor(@Valid @RequestBody AuthorCreationRequest authorCreationRequest){
        log.info(String.format("Updating author with name: %s",authorCreationRequest.getAuthorName()));

        this.authorService.updateAuthor(authorCreationRequest.getAuthorName());

        log.info("Author was successfully updated!");

        return ResponseEntity.ok().build();

    }
}
