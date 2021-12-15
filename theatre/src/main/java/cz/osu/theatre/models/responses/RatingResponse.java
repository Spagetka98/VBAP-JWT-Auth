package cz.osu.theatre.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RatingResponse {
    private int ratingValue;
    private String username;
    private String email;
}
