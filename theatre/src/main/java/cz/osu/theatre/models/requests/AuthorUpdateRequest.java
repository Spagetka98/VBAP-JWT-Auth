package cz.osu.theatre.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AuthorUpdateRequest {
    private long idAuthor;

    @NotBlank(message = "Author firstName cannot be blank!")
    @Size(min = 2,message = "Minimal size of author name is at least 2 characters")
    private String firstName;

    @NotBlank(message = "Author lastName cannot be blank!")
    @Size(min = 3,message = "Minimal size of author name is at least 3 characters")
    private String lastName;
}
