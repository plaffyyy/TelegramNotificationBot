package backend.academy.bot.notifier.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.time.LocalTime;

@Getter
@Configuration
public class NotificationConfig {
    @Value("${bot.notification.mode}")
    private NotificationMode mode;

    @Value("${bot.notification.digest-time}")
    private String digestTime;

    private LocalTime digestLocalTime;

    @PostConstruct
    public void init() {
        this.digestLocalTime = LocalTime.parse(digestTime);
    }

    public enum NotificationMode {
        IMMEDIATE,
        DAILY_DIGEST
    }

    public String getDigestCronTime() {
        return String.format("0 %d %d * * *",
            digestLocalTime.getMinute(),
            digestLocalTime.getHour()
        );
    }

}
