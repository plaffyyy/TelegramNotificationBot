package backend.academy.bot.controllers.kafka;

import backend.academy.bot.dto.LinkUpdateRequest;
import backend.academy.bot.notifier.Notifier;
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

    @KafkaListener(
        topics = "${spring.kafka.topic.updates}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(
        @Payload LinkUpdateRequest request,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset
    ) {
        try {
            log.info("Received message: partition={}, offset={}, request={}", partition, offset, request);
            
            List<Long> ids = request.ids() == null ? List.of() : request.ids();
            String url = request.url();
            String description = request.description();

            StringBuilder message = new StringBuilder();
            message.append("📢 Уведомление!\nНовое обновление в ссылке: ")
                .append(url)
                .append("\n");
            message.append(description);
            
            notifier.notifyUsers(ids, message.toString());
            
            log.info("Successfully processed message: partition={}, offset={}", partition, offset);
        } catch (Exception e) {
            log.error("Error processing message: partition={}, offset={}, error={}", 
                partition, offset, e.getMessage(), e);
            // You might want to implement retry logic or dead letter queue here
            throw e; // Rethrowing to let Kafka know the message wasn't processed
        }
    }
}
