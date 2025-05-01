package backend.academy.bot.notifier;

import backend.academy.bot.notifier.config.NotificationConfig;
import backend.academy.bot.services.NotificationRedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class NotificationScheduler {

    private final NotificationRedisCache notificationRedisCache;
    private final NotificationConfig notificationConfig;
    private final Notifier notifier;


    @Scheduled(cron = "#{@notificationConfig.getDigestCronTime()}")
    public void sendDailyDigest() {
        if (notificationConfig.mode() == NotificationConfig.NotificationMode.DAILY_DIGEST) {
            sendDigest();
        }
    }

    private void sendDigest() {
        log.info("Starting to send daily digest notifications");

        notificationRedisCache.getAllChatIdsFromCache().forEach(key -> {
            try {
                Long chatId = Long.parseLong(key.replace(NotificationRedisCache.NOTIFICATION_CACHE_KEY_PREFIX, ""));
                List<String> notifications = notificationRedisCache.getNotificationsByChatId(chatId);

                if (!notifications.isEmpty()) {
                    StringBuilder digestMessage = new StringBuilder();
                    digestMessage.append("📅 Ежедневный дайджест обновлений\n\n");

                    notifications.forEach(notification -> {
                        digestMessage.append(notification).append("\n\n");
                    });

                    notifier.notifyUsers(List.of(chatId), digestMessage.toString());
                    notificationRedisCache.clearNotificationsByChatId(chatId);
                }
            } catch (Exception e) {
                log.error("Error sending digest for key: {}", key, e);
            }
        });

        log.info("Finished sending daily digest notifications");
    }

}
