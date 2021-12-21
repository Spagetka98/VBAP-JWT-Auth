package cz.osu.theatre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
public class TheatreApplication {
    public static void main(String[] args) {
        SpringApplication.run(TheatreApplication.class, args);
    }
}