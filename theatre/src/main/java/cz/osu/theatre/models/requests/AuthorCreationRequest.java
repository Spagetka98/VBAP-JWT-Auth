package cz.osu.theatre.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AuthorCreationRequest {
    @NotBlank(message = "Author name cannot be blank!")
    @Size(min = 5,message = "Minimal size of author name is at least 5 characters")
    private String authorName;
}
