package backend.academy.bot.notifier;

import backend.academy.bot.notifier.config.NotificationConfig;
import backend.academy.bot.services.NotificationRedisCache;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationHandler {

    private final Notifier notifier;
    private final NotificationConfig notificationConfig;
    private final NotificationRedisCache notificationRedisCache;

    public void handleNotification(String url, String description, List<Long> ids) {
        StringBuilder message = new StringBuilder();
        message.append("📢 Уведомление!\nНовое обновление в ссылке: ")
                .append(url)
                .append("\n");
        message.append(description);

        if (notificationConfig.mode().equals(NotificationConfig.NotificationMode.IMMEDIATE)) {
            notifier.notifyUsers(ids, message.toString());
        } else if (notificationConfig.mode().equals(NotificationConfig.NotificationMode.DAILY_DIGEST)) {
            ids.forEach(id -> notificationRedisCache.addNotification(id, message.toString()));
        } else {
            log.error("Error in configuration mode in application.yaml");
        }
    }
}
