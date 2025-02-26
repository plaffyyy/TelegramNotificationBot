package backend.academy.scrapper.notifier_tests;

import backend.academy.scrapper.updates.LinkUpdateChecker;
import backend.academy.scrapper.updates.UpdateScheduler;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//extend the spring context for scheduler test
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableScheduling // включаем планировщик в тестах
public class SchedulerTests {

    @Test
    public void testSchedulerRuns() {
        LinkUpdateChecker mockLinkUpdateChecker = mock(LinkUpdateChecker.class);
        AtomicBoolean wasCalled = new AtomicBoolean(false);

        doAnswer(invocation -> {
            System.out.println("checkForUpdates() вызван");
            wasCalled.set(true);
            return null;
        }).when(mockLinkUpdateChecker).checkForUpdates();

        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();

        UpdateScheduler updateScheduler = new UpdateScheduler(mockLinkUpdateChecker);
        taskScheduler.schedule(updateScheduler::updateCheck, triggerContext -> {
            return new Date(System.currentTimeMillis() + 30_000).toInstant();
        });
        Awaitility.await()
            .atMost(35, TimeUnit.SECONDS)
            .untilTrue(wasCalled);

        verify(mockLinkUpdateChecker, atLeastOnce()).checkForUpdates();
    }
}
