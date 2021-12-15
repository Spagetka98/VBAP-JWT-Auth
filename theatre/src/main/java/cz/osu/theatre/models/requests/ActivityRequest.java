package cz.osu.theatre.models.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ActivityRequest {
    @NotBlank(message = "Activity name cannot be blank!")
    @Size(min = 5,message = "Activity name must be at least 5 characters long!")
    private String name;

    @NotBlank(message = "Stage cannot be blank!")
    @Size(min = 5,message = "Stage must be at least 5 characters long!")
    private String stage;

    @NotNull(message = "Date cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotBlank(message = "Start time cannot be blank")
    @Size(min = 7,max = 8, message = "Start should be in format 'hour':'minutes':'seconds' ")
    private String start;

    private String end;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "URL cannot be blank")
    private String url;

    private List<Long> divisionIds;

    private List<Long> authorIds;
}
