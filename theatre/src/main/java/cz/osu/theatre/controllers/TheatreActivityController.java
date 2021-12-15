package cz.osu.theatre.controllers;

import cz.osu.theatre.models.entities.TheatreActivity;
import cz.osu.theatre.models.requests.ActivityRequest;
import cz.osu.theatre.models.responses.TheatreActivityResponse;
import cz.osu.theatre.services.TheatreActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/theatreActivity")
@RequiredArgsConstructor
public class TheatreActivityController {
    private final TheatreActivityService theatreActivityService;

    @GetMapping("/getTheatreActivitiesForPage")
    public ResponseEntity<?> getTheatreActivitiesForAuthor(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int activitiesPerPage){
        log.info("Loading theatre activities");

        List<TheatreActivity> theatreActivities = this.theatreActivityService.getTheatreActivities(PageRequest.of(page,activitiesPerPage));

        log.info("Theatre activities were successfully loaded!");

        return ResponseEntity.ok(theatreActivities.stream().map(TheatreActivityResponse::new));
    }


    @GetMapping("/getTheatreActivitiesForAuthor")
    public ResponseEntity<?> getTheatreActivitiesForAuthor(
            @RequestParam long idAuthor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int activitiesPerPage){
        log.info(String.format("Loading theatre activities of author with id: %d",idAuthor));

        List<TheatreActivity> theatreActivities = this.theatreActivityService.getTheatreActivitiesForAuthor(idAuthor, PageRequest.of(page,activitiesPerPage));

        log.info("Theatre activities were successfully loaded!");

        return ResponseEntity.ok(theatreActivities.stream().map(TheatreActivityResponse::new));
    }

    @GetMapping("/getTheatreActivitiesForDivision")
    public ResponseEntity<?> getTheatreActivitiesForDivision(
            @RequestParam List<Long> idsDivision,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int activitiesPerPage){
        log.info(String.format("Loading theatre activities with divisions with ids: %s",idsDivision.toString()));

        List<TheatreActivity> theatreActivities = this.theatreActivityService.getTheatreActivitiesOfDivisions(idsDivision, PageRequest.of(page,activitiesPerPage));

        log.info("Theatre activities were successfully loaded!");

        return ResponseEntity.ok(theatreActivities.stream().map(TheatreActivityResponse::new));
    }

    @GetMapping("/getTop5TheatreActivities")
    public ResponseEntity<?> getTop5TheatreActivities(){
        log.info("Loading top 5 theatre activities");

        List<TheatreActivity> theatreActivities = this.theatreActivityService.getTopRatedTheatreActivities();

        log.info("Theatre activities were successfully loaded!");

        return ResponseEntity.ok(theatreActivities.stream().map(TheatreActivityResponse::new));
    }

    @PostMapping("/createTheatreActivity")
    public ResponseEntity<?> createTheatreActivity(@Valid @RequestBody ActivityRequest activityRequest){
        log.info("Creating theatre activity");

        this.theatreActivityService.createTheatreActivityFromRequest(activityRequest);

        log.info("Theatre activity was successfully created!");

        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateTheatreActivity/{idActivity}")
    public ResponseEntity<?> updateTheatreActivity(@PathVariable long idActivity,@Valid @RequestBody ActivityRequest activityRequest){
        log.info("Updating theatre activity");

        this.theatreActivityService.updateTheatreActivityFromRequest(idActivity,activityRequest);

        log.info("Theatre activity was successfully updated!");

        return ResponseEntity.ok().build();
    }
}
