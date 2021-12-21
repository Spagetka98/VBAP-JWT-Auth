package cz.osu.theatre.services;

import cz.osu.theatre.errors.exceptions.*;
import cz.osu.theatre.models.entities.Division;
import cz.osu.theatre.models.entities.TheatreActivity;
import cz.osu.theatre.repositories.DivisionRepository;
import cz.osu.theatre.repositories.TheatreActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DivisionServiceImpl implements DivisionService {
    private final DivisionRepository divisionRepository;
    private final TheatreActivityRepository theatreActivityRepository;

    @Override
    public Set<Division> checkDivisions(Set<Division> divisions) {
        Set<Division> divisionSet = new HashSet<>();

        divisions.forEach(division -> this.divisionRepository.findByName(division.getName())
                .ifPresentOrElse(divisionSet::add, () -> {
                    Division newDivision = new Division(division.getName());
                    divisionSet.add(this.divisionRepository.save(newDivision));
                }));

        return divisionSet;
    }

    @Override
    public List<Division> getAllDivisions() {
        return this.divisionRepository.findAll();
    }

    @Override
    public List<Division> getAllDivisionsForActivityId(long idTheatreActivity) {
        TheatreActivity theatreActivity = this.theatreActivityRepository.findById(idTheatreActivity)
                .orElseThrow(() -> new TheatreActivityNotFoundException(String.format("Could not find a theatre activity with id: %d", idTheatreActivity)));

        return theatreActivity.getDivisions().stream().toList();
    }

    @Override
    public void createDivision(String name) {
        if (name == null || name.trim().isEmpty())
            throw new ParameterException("Name for division cannot be null or empty!");

        if(this.divisionRepository.findByName(name).isPresent()){
            throw new DivisionAlreadyExistException(String.format("Could not create a division with name: %s",name));
        }

        Division newDivision = new Division(name);
        this.divisionRepository.save(newDivision);
    }

    @Override
    public void updateDivision(long idDivision, String newDivisionName) {
        if (newDivisionName == null || newDivisionName.trim().isEmpty())
            throw new ParameterException("Parameter newDivisionName cannot be null or empty!");

        Division oldDivision = this.divisionRepository.findById(idDivision)
                .orElseThrow(() -> new DivisionNotFoundException(String.format("Could not find a division with id: %d", idDivision)));

        if(this.divisionRepository.findByName(newDivisionName).isPresent()){
            throw new DivisionAlreadyExistException(String.format("Could not update division with name: %s due to duplication",newDivisionName));
        }

        oldDivision.setName(newDivisionName);
        this.divisionRepository.save(oldDivision);
    }

    @Override
    public void deleteDivision(long idDivision) {
        Division division = this.divisionRepository.findById(idDivision)
                .orElseThrow(() -> new DivisionNotFoundException(String.format("Could not find a division with id: %d", idDivision)));

        int divisionAssociations = division.getTheatreActivities().size();
        if (divisionAssociations > 0)
            throw new DivisionIsUsedException(String.format("Could not delete division with id: %d because division has associations with: %d activities", idDivision, divisionAssociations));

        this.divisionRepository.delete(division);
    }

}
