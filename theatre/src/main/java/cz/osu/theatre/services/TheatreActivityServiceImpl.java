package cz.osu.theatre.services;

import cz.osu.theatre.errors.exceptions.AuthorNotFoundException;
import cz.osu.theatre.errors.exceptions.ParameterException;
import cz.osu.theatre.errors.exceptions.TheatreActivitySaveException;
import cz.osu.theatre.models.entities.Author;
import cz.osu.theatre.models.entities.Division;
import cz.osu.theatre.models.entities.TheatreActivity;
import cz.osu.theatre.models.loaders.XmlLoader;
import cz.osu.theatre.models.requests.ActivityRequest;
import cz.osu.theatre.repositories.AuthorRepository;
import cz.osu.theatre.repositories.DivisionRepository;
import cz.osu.theatre.repositories.TheatreActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TheatreActivityServiceImpl implements TheatreActivityService{
    private final TheatreActivityRepository theatreActivityRepository;
    private final DivisionRepository divisionRepository;
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;
    private final DivisionService divisionService;

    @Override
    public void saveAllNewActivities() {
        try {
            XmlLoader xmlLoader = new XmlLoader();

            xmlLoader.getElementsNodes()
                    .forEach((element -> {
                        xmlLoader.createActivity(element)
                                .ifPresent(activity -> {
                                    Set<Author> authors = xmlLoader.createAuthors(element);
                                    Set<Division> divisions = xmlLoader.createDivisions(element);

                                    this.mergeTheatreActivity(activity,authors,divisions);
                                });
                    }));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error("An error occurred while saving new Theatre activities");
            log.error("Error message: " + e.getMessage());
            throw new TheatreActivitySaveException("Could not save theatre activities",e);
        }
    }

    @Transactional
    @Override
    public void mergeTheatreActivity(TheatreActivity activity, Set<Author> authors, Set<Division> divisions){
        activity.setAuthors(this.authorService.checkAuthors(authors));
        activity.setDivisions(this.divisionService.checkDivisions(divisions));

        this.theatreActivityRepository.save(activity);
    }

    @Override
    public List<TheatreActivity> getTheatreActivitiesForAuthor(long idAuthor, Pageable pageable) {
        Author author = this.authorRepository.findById(idAuthor)
                .orElseThrow(() -> new AuthorNotFoundException(String.format("Could not find a author with id: %d",idAuthor)));

        return this.theatreActivityRepository.findByAuthors_id(author.getId(),pageable);
    }

    @Override
    public List<TheatreActivity> getTheatreActivitiesOfDivisions(List<Long> idsDivision, Pageable pageable) {
        return this.theatreActivityRepository.findByDivisions_idIn(idsDivision,pageable);
    }

    @Override
    public List<TheatreActivity> getTopRatedTheatreActivities() {
        return this.theatreActivityRepository.findTop5ByRatingGreaterThanOrderByRatingDesc(-1);
    }

    @Override
    public List<TheatreActivity> getTheatreActivities(Pageable pageable) {
        return this.theatreActivityRepository.findAll(pageable).stream().toList();
    }

    @Override
    public void createTheatreActivityFromRequest(ActivityRequest activityRequest) {
        TheatreActivity newTheatreActivity = validateTheatreActivityFromRequest(activityRequest);

        this.theatreActivityRepository.save(newTheatreActivity);
    }

    @Override
    public void updateTheatreActivityFromRequest(long idActivity, ActivityRequest activityRequest) {
        TheatreActivity oldActivity = this.theatreActivityRepository.findById(idActivity)
                .orElseThrow(()->new ParameterException(String.format("Could not find a activity with id: %d",idActivity)));

        TheatreActivity newTheatreActivity = validateTheatreActivityFromRequest(activityRequest);

        oldActivity.setName(newTheatreActivity.getName());
        oldActivity.setStage(newTheatreActivity.getStage());
        oldActivity.setDate(newTheatreActivity.getDate());
        oldActivity.setStart(newTheatreActivity.getStart());
        oldActivity.setEnd(newTheatreActivity.getEnd());
        oldActivity.setDescription(newTheatreActivity.getDescription());
        oldActivity.setUrl(newTheatreActivity.getUrl());
        oldActivity.setRatings(newTheatreActivity.getRatings());
        oldActivity.setDivisions(newTheatreActivity.getDivisions());

        this.theatreActivityRepository.save(oldActivity);
    }

    private TheatreActivity validateTheatreActivityFromRequest(ActivityRequest activityRequest) {
        String activityName = parameterCheck(activityRequest.getName(),5);
        String stage = parameterCheck(activityRequest.getStage(),3);
        LocalDate date = activityRequest.getDate();

        String startTime = timeCheck(activityRequest.getStart());

        String endTime = activityRequest.getEnd();
        if(endTime != null) endTime = timeCheck(endTime);

        String description = parameterCheck(activityRequest.getDescription(),-1);
        String url = parameterCheck(activityRequest.getUrl(),4);

        Set<Division> divisions = new HashSet<>();
        activityRequest.getDivisionIds().forEach((idDivision)->{
            divisions.add(this.divisionRepository.findById(idDivision).
                    orElseThrow(()-> new ParameterException(String.format("Could not find a division with id: %d",idDivision))));
        });

        Set<Author> authors = new HashSet<>();
        activityRequest.getAuthorIds().forEach((idAuthor)->{
            authors.add(this.authorRepository.findById(idAuthor).
                    orElseThrow(()-> new ParameterException(String.format("Could not find a author with id: %d",idAuthor))));
        });

        TheatreActivity newTheatreActivity = new TheatreActivity(activityName,stage,date,startTime,endTime,description,url,0);
        newTheatreActivity.setDivisions(divisions);
        newTheatreActivity.setAuthors(authors);
        return newTheatreActivity;
    }

    private String parameterCheck(String parameter,int minLength){
        if(parameter == null || parameter.trim().isEmpty() || parameter.length() < 5) throw new ParameterException(String.format("%s cannot be null or empty and must be at least %d characters long!",parameter,minLength));
        return parameter;
    }

    private String timeCheck(String parameter){
        String pattern = "HH:mm:ss";
        DateTimeFormatter strictTimeFormatter = DateTimeFormatter
                .ofPattern(pattern)
                .withResolverStyle(ResolverStyle.STRICT);

        try {
            LocalTime.parse(parameter, strictTimeFormatter);
        }catch (DateTimeParseException | NullPointerException e){
            throw new ParameterException(String.format("Could not convert parameter: %s to time pattern: %s",parameter,pattern));
        }

        return parameter;
    }
}
