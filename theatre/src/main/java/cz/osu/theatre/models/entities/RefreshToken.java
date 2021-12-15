package cz.osu.theatre.models.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "refreshToken")
@ToString(of = {"id", "token","expiryDate"})
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    @NotBlank(message = "Token cannot be blank!")
    private String token;
    @NotNull(message = "Expiration date cannot be null!")
    private Instant expiryDate;

    public RefreshToken(@NonNull String token, @NonNull Instant expiryDate,@NonNull AppUser user) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.appUser = user;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),getToken(),getExpiryDate());
    }
}
