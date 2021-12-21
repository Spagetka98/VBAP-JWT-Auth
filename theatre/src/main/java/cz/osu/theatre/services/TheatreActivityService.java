package cz.osu.theatre.services;

import cz.osu.theatre.models.entities.Author;
import cz.osu.theatre.models.entities.Division;
import cz.osu.theatre.models.entities.TheatreActivity;
import cz.osu.theatre.models.requests.ActivityRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface TheatreActivityService {
    void saveAllNewActivities();

    void mergeTheatreActivity(TheatreActivity activity, Set<Author> authors, Set<Division> divisions);

    List<TheatreActivity> getTheatreActivitiesForAuthor(long idAuthor, Pageable pageable);

    List<TheatreActivity> getTheatreActivitiesOfDivisions(List<Long> idsDivision, Pageable pageable);

    List<TheatreActivity> getTopRatedTheatreActivities();

    List<TheatreActivity> getTheatreActivities(Pageable pageable);

    void createTheatreActivityFromRequest(ActivityRequest activityRequest);

    void updateTheatreActivityFromRequest(long idActivity, ActivityRequest activityRequest);

    void deleteTheatreActivity(long idActivity);
}
