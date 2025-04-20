package backend.academy.bot.notifier.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.time.LocalTime;

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

    public NotificationMode getMode() {
        return mode;
    }

    public void setMode(NotificationMode mode) {
        this.mode = mode;
    }

    public LocalTime getDigestTime() {
        return digestLocalTime;
    }

    public void setDigestTime(LocalTime digestTime) {
        this.digestLocalTime = digestTime;
    }
} 