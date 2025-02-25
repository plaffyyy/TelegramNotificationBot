package backend.academy.scrapper.updates;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class UpdateScheduler {

    @Autowired
    private final LinkUpdateChecker linkUpdateChecker;

    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.SECONDS)
    public void updateCheck() {

        linkUpdateChecker.checkForUpdates();

    }

}
