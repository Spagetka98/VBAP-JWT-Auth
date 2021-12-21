package cz.osu.theatre.models.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "author")
@Data
@NoArgsConstructor
@ToString(of = {"id", "firstName","lastName"})
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "FirstName cannot be blank!")
    private String firstName;

    private String lastName;

    @ManyToMany(mappedBy = "authors")
    private Set<TheatreActivity> theatreActivities = new HashSet<>();

    public Author(String name) {
        this(name,null);
    }

    public Author(String firstName,String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),getFirstName(),getLastName());
    }
}
