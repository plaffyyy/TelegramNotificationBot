package backend.academy.scrapper.services.updateSend.kafka;

import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.model.LinkUpdateRequest;
import backend.academy.scrapper.services.updateSend.SendNotification;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "message-transport", havingValue = "Kafka")
public class SendNotificationKafka implements SendNotification {

    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final Random random = new Random();

    @Value("${spring.kafka.topic.updates}")
    private String updatesTopic;

    @Value("${spring.kafka.topic.updates}.DLT")
    private String deadLetterTopic;

    @Override
    public void sendUpdateToBot(Link link, List<Long> ids, String description) {
        // добавил id реквеста в виде строки, чтобы в случае совпадения добавлялось в одну партицию
        // потом когда id будет не рандомным числом, а реальным id ссылки, будет работать отлично
        try {
            // Validate the message
            validateMessage(link, description);

            LinkUpdateRequest request = new LinkUpdateRequest(random.nextLong(), link.url(), description, ids);

            // Send to main topic
            log.info("Sending update to topic {}: {}", updatesTopic, request);
            kafkaTemplate
                    .send(updatesTopic, String.valueOf(request.id()), request)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Successfully sent message to topic {}: {}", updatesTopic, request);
                        } else {
                            log.error("Failed to send message to topic {}: {}", updatesTopic, ex.getMessage());
                            sendToDLQ(request, "Failed to send message: " + ex.getMessage());
                        }
                    });

        } catch (IllegalArgumentException e) {
            // Handle validation errors
            sendToDLQWithExceptionHandling(e, link, ids);
        } catch (Exception e) {
            // Handle other unexpected errors
            sendToDLQWithExceptionHandling(e, link, ids);
        }
    }

    private void validateMessage(Link link, String description) {
        // TODO: сделать проверку более корректную
        if (link == null || link.url() == null || link.url().isEmpty()) {
            throw new IllegalArgumentException("Link or URL is null or empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description is null or empty");
        }
    }

    private void sendToDLQ(LinkUpdateRequest request, String errorMessage) {
        try {
            log.info("Sending message to DLQ topic {}: {} (Error: {})", deadLetterTopic, request, errorMessage);
            kafkaTemplate
                    .send(deadLetterTopic, String.valueOf(request.id()), request)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Successfully sent message to DLQ topic {}", deadLetterTopic);
                        } else {
                            log.error("Failed to send message to DLQ topic: {}", ex.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.error("Failed to send message to DLQ: {}", e.getMessage());
        }
    }

    private void sendToDLQWithExceptionHandling(Exception e, Link link, List<Long> ids) {
        if (e instanceof IllegalArgumentException) {
            log.error("Validation error for link {}: {}", link.url(), e.getMessage());
        } else {
            log.error("Unexpected error while sending update for link {}: {}", link.url(), e.getMessage());
        }
        LinkUpdateRequest failedRequest = new LinkUpdateRequest(
                random.nextLong(),
                link.url(),
                e instanceof IllegalArgumentException ? e.getMessage() : "Failed to process update",
                ids);
        sendToDLQ(failedRequest, "Error: " + e.getMessage());
    }
}
