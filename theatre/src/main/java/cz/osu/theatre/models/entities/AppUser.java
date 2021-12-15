package cz.osu.theatre.models.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "appUser")
@ToString(of = {"id", "username","email"})
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 5, max = 30,message = "Minimum length of username is 5 characters and maximum length of username is 30 characters!")
    private String username;

    @NotBlank(message = "Email cannot be blank!")
    @Size(max = 40,message = "Maximum length of email is 40 characters!")
    @Email(message = "Email of AppUser is not valid!")
    private String email;

    @NotBlank(message = "Password cannot be blank!")
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refreshToken_id")
    private RefreshToken refreshToken;

    @OneToMany(mappedBy = "ratingCreator", cascade = CascadeType.ALL)
    private Set<Rating> ratings = new HashSet<>();

    public AppUser(@NonNull String username, @NonNull String email, @NonNull String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),getUsername(),getEmail());
    }
}
