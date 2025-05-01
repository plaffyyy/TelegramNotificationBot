package backend.academy.bot.controllers;

import backend.academy.bot.notifier.Notifier;
import java.util.List;
import java.util.Map;
import backend.academy.bot.notifier.config.NotificationConfig;
import backend.academy.bot.services.NotificationRedisCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/updates")
public class UpdatesController {

    private final Notifier notifier;
    private final NotificationConfig notificationConfig;
    private final NotificationRedisCache notificationRedisCache;

    @PostMapping
    public ResponseEntity<Void> postUpdate(@RequestBody Map<String, Object> request) {
        List<Long> ids = (List<Long>) request.getOrDefault("tgChatIds", List.of());
        String url = (String) request.get("url");
        String description = (String) request.get("description");

        StringBuilder message = new StringBuilder();
        message.append("📢 Уведомление!\nНовое обновление в ссылке: ")
                .append(url)
                .append("\n");
        message.append(description);


        if (notificationConfig.mode().equals(NotificationConfig.NotificationMode.IMMEDIATE)) {
            notifier.notifyUsers(ids, message.toString());
        } else if (notificationConfig.mode().equals(NotificationConfig.NotificationMode.DAILY_DIGEST)){
            ids.forEach(id -> notificationRedisCache.addNotification(id, message.toString()));
        } else {
            log.error("Error in configuration mode in application.yaml");
        }

        return ResponseEntity.ok(null);
    }
}
