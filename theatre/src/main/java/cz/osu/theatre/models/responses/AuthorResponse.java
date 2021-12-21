package cz.osu.theatre.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorResponse {
    private long id;
    private String firstName;
    private String lastName;
}
