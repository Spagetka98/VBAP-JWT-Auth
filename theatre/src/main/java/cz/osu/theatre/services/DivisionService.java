package cz.osu.theatre.services;

import cz.osu.theatre.models.entities.Division;

import java.util.List;
import java.util.Set;

public interface DivisionService {
    Set<Division> checkDivisions(Set<Division> divisions);

    List<Division> getAllDivisions();

    List<Division> getAllDivisionsForActivityId(long idTheatreActivity);

    void createDivision(String name);

    void updateDivision(long idDivision, String newDivisionName);

    void deleteDivision(long idDivision);
}
