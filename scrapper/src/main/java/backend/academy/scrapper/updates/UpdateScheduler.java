package backend.academy.scrapper.updates;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
@AllArgsConstructor
public class UpdateScheduler {

    @Autowired
    private final LinkUpdateChecker linkUpdateChecker;

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void updateCheck() {

        linkUpdateChecker.checkForUpdates();

    }

}
