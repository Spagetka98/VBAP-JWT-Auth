package cz.osu.theatre.models.responses;

import cz.osu.theatre.models.entities.Division;
import cz.osu.theatre.models.entities.TheatreActivity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class TheatreActivityResponse {
    private long id;
    private String name;
    private String stage;
    private LocalDate date;
    private String start;
    private String end;
    private String description;
    private Set<String> divisionsName;
    private double rating;

    public TheatreActivityResponse(TheatreActivity theatreActivity){
        this.id = theatreActivity.getId();
        this.name = theatreActivity.getName();
        this.stage = theatreActivity.getStage();
        this.date = theatreActivity.getDate();
        this.start = theatreActivity.getStart();
        this.end = theatreActivity.getEnd();
        this.description = theatreActivity.getDescription();
        this.divisionsName = theatreActivity.getDivisions().stream().map(Division::getName).collect(Collectors.toSet());
        this.rating = theatreActivity.getRating();
    }
}
