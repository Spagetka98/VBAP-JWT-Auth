package cz.osu.theatre.models.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;

@Entity(name = "rating")
@Data
@NoArgsConstructor
@ToString(of = {"id", "ratingValue"})
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Max(5)
    @Min(0)
    private int ratingValue;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appUser_id")
    private AppUser ratingCreator;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "theatreActivity_id")
    private TheatreActivity theatreActivity;

    public Rating(int ratingValue, AppUser ratingCreator, TheatreActivity theatreActivity) {
        this.ratingValue = ratingValue;
        this.ratingCreator = ratingCreator;
        this.theatreActivity = theatreActivity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),getRatingValue());
    }
}
