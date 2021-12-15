package cz.osu.theatre.schedulers;

import cz.osu.theatre.services.TheatreActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TheatreActivitiesLoader {
    private final TheatreActivityService theatreActivityService;

    //11PM o'clock of every day
    @Scheduled(cron = "0 0 23 * * *" )
    //@Scheduled(cron="*/50 * * * * ?")
    public void loadData() {
        log.info("Start loading new theatre activities");

        this.theatreActivityService.saveAllNewActivities();

        log.info("End of loading new theatre activities");
    }
}
