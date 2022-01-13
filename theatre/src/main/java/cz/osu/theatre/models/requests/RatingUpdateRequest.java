package cz.osu.theatre.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
public class RatingUpdateRequest {
    private long idActivity;
    @Min(value = 0,message = "Minimum rating is 0")
    @Max(value = 5,message = "Maximum rating is 5")
    private int rating;
}
