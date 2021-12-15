package cz.osu.theatre.models.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "division")
@Data
@NoArgsConstructor
@ToString(of = {"id", "name"})
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank!")
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "divisions")
    private Set<TheatreActivity> theatreActivities = new HashSet<>();

    public Division(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),getName());
    }
}
