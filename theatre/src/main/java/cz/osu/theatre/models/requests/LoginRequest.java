package cz.osu.theatre.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {
    @NotBlank
    @Size(min = 5, max = 30,message = "Minimum size of username is 5 characters and maximum size of username is 30 characters!")
    private String username;
    @NotBlank
    private String password;
}
