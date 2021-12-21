package cz.osu.theatre.controllers;

import cz.osu.theatre.models.entities.Division;
import cz.osu.theatre.models.requests.*;
import cz.osu.theatre.models.responses.DivisionResponse;
import cz.osu.theatre.services.DivisionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/division")
@RequiredArgsConstructor
public class DivisionController {
    private final DivisionService divisionService;

    @GetMapping("/getAllDivisions")
    public ResponseEntity<?> getAllDivisions(){
        log.info("Loading all divisions");

        List<Division> divisions = this.divisionService.getAllDivisions();

        log.info("Divisions was successfully loaded!");

        return ResponseEntity.ok(divisions.stream().map((division) -> new DivisionResponse(division.getId(),division.getName())).toList());
    }

    @GetMapping("/getAllDivisionsForActivity/{idTheatreActivity}")
    public ResponseEntity<?> getAllDivisionsForActivity(@PathVariable long idTheatreActivity){
        log.info(String.format("Loading all divisions for activity with id: %d", idTheatreActivity));

        List<Division> divisions = this.divisionService.getAllDivisionsForActivityId(idTheatreActivity);

        log.info("Divisions was successfully loaded!");

        return ResponseEntity.ok(divisions.stream().map((division) -> new DivisionResponse(division.getId(),division.getName())).toList());
    }

    @PostMapping("/createDivision")
    public ResponseEntity<?> createDivision(@Valid @RequestBody DivisionCreationRequest divisionCreationRequest){
        log.info(String.format("Creating division with name: %s",divisionCreationRequest.getName()));

        this.divisionService.createDivision(divisionCreationRequest.getName());

        log.info("Division was successfully created!");

        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateDivision")
    public ResponseEntity<?> updateDivision(@Valid @RequestBody DivisionUpdateRequest divisionUpdateRequest){
        log.info(String.format("Updating division for name: %s", divisionUpdateRequest.getNewDivisionName()));

        this.divisionService.updateDivision(divisionUpdateRequest.getIdDivision(),divisionUpdateRequest.getNewDivisionName());

        log.info("Division was successfully updated!");

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteDivision/{idDivision}")
    public ResponseEntity<?> deleteDivision(@PathVariable long idDivision){
        log.info(String.format("Deleting division with id: %d", idDivision));

        this.divisionService.deleteDivision(idDivision);

        log.info("Deleting was successfully updated!");

        return ResponseEntity.ok().build();
    }
}
