package backend.academy.bot.controllers.kafka;

import backend.academy.bot.dto.LinkUpdateRequest;
import backend.academy.bot.notifier.NotificationScheduler;
import backend.academy.bot.notifier.Notifier;
import backend.academy.bot.notifier.config.NotificationConfig;
import backend.academy.bot.services.NotificationRedisCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class LinkUpdateListener {

    private final Notifier notifier;
    private final NotificationConfig notificationConfig;
    private final NotificationRedisCache notificationRedisCache;

    @KafkaListener(
        topics = "${spring.kafka.topic.updates}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory",
        batch = "true"
    )
    public void listen(
        @Payload List<LinkUpdateRequest> requests,
        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
        @Header(KafkaHeaders.OFFSET) List<Long> offsets
    ) {
        log.info("Received batch of {} messages", requests.size());

        for (int i = 0; i < requests.size(); i++) {
            LinkUpdateRequest request = requests.get(i);
            int partition = partitions.get(i);
            long offset = offsets.get(i);

            try {
                log.info("Processing message: partition={}, offset={}, request={}",
                    partition, offset, request);

                List<Long> ids = request.ids() == null ? List.of() : request.ids();
                String url = request.url();
                String description = request.description();

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

                log.info("Successfully processed message: partition={}, offset={}",
                    partition, offset);
            } catch (Exception e) {
                log.error("Error processing message: partition={}, offset={}, error={}",
                    partition, offset, e.getMessage(), e);
                // Individual message failure doesn't stop batch processing
            }
        }
    }
}
