package cz.osu.theatre.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpRequest {
    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 5, max = 30,message = "Minimum length of username is 5 characters and maximum length of username is 30 characters!")
    private String username;

    @NotBlank(message = "Email cannot be blank!")
    @Size(max = 40,message = "Maximum length of email is 40 characters!")
    @Email(message = "Email of AppUser is not valid!")
    private String email;

    @NotBlank(message = "Password cannot be blank!")
    @Size(min = 6, max = 40,message = "Minimum length of password is 6 characters and maximum length is 40 characters!")
    private String password;
}
