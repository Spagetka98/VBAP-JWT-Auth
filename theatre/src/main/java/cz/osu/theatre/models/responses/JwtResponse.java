package cz.osu.theatre.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private long id;
    private String username;
    private String email;
    private String accessToken;
    private String refreshToken;
}
