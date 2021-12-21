package cz.osu.theatre.models.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "theatreActivity")
@Data
@NoArgsConstructor
@ToString(of = {"id", "name", "stage", "date", "start",
        "description", "url", "rating"})
public class TheatreActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ndmId;

    @NotBlank(message = "Name cannot be blank!")
    private String name;

    @NotBlank(message = "Stage cannot be blank")
    private String stage;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @NotBlank(message = "Start time cannot be blank")
    private String start;

    private String end;

    @NotBlank(message = "Description cannot be blank")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String description;

    @NotBlank(message = "URL cannot be blank")
    private String url;

    @Max(value = 5)
    @Min(value = 0)
    private double rating;

    @OneToMany(mappedBy = "theatreActivity", cascade = CascadeType.ALL)
    private Set<Rating> ratings = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "theatreActivity_author",
            joinColumns = @JoinColumn(name = "theatreActivity_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "theatreActivity_divisions",
            joinColumns = @JoinColumn(name = "theatreActivity_id"),
            inverseJoinColumns = @JoinColumn(name = "division_id"))
    private Set<Division> divisions = new HashSet<>();

    public TheatreActivity(@NonNull String name, @NonNull String stage, @NonNull LocalDate date, @NonNull String start,
                           String end, @NonNull String description, @NonNull String url, double rating) {
        this.name = name;
        this.stage = stage;
        this.date = date;
        this.start = start;
        this.end = end;
        this.description = description;
        this.url = url;
        this.rating = rating;
    }

    public TheatreActivity(@NonNull String name, Long ndmId, @NonNull String stage, @NonNull LocalDate date, @NonNull String start,
                           String end, @NonNull String description, @NonNull String url, double rating) {
        this.name = name;
        this.ndmId = ndmId;
        this.stage = stage;
        this.date = date;
        this.start = start;
        this.end = end;
        this.description = description;
        this.url = url;
        this.rating = rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getName(), getStage(), getStart());
    }

    @Override
    public boolean equals(Object activity) {
        if (activity == this) return true;

        if (activity == null || activity.getClass() != this.getClass()) return false;

        TheatreActivity guest = (TheatreActivity) activity;
        return this.getId().compareTo(guest.getId()) == 0;
    }
}