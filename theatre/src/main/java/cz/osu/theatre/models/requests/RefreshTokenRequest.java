package cz.osu.theatre.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh Token cannot be blank!")
    private String refreshToken;
}