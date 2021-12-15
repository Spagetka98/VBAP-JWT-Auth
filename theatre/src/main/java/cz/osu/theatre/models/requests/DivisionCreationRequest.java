package cz.osu.theatre.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DivisionCreationRequest {
    @NotBlank(message = "Division name cannot be empty!")
    private String name;
}
