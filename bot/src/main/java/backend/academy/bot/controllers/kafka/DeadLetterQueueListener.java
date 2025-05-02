package backend.academy.bot.controllers.kafka;

import backend.academy.bot.dto.LinkUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeadLetterQueueListener {

    @KafkaListener(
            topics = "${spring.kafka.topic.updates}.DLT",
            groupId = "${spring.kafka.consumer.group-id}-dlq",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenDLQ(
            @Payload LinkUpdateRequest request,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage) {
        log.error(
                "Dead Letter Queue message: partition={}, offset={}, request={}, error={}",
                partition,
                offset,
                request,
                errorMessage);
        // There are ways how can handle DLQ messages(for this task log is enough):
        // 1. Send notifications to administrators
        // 2. Store failed messages in a database
        // 3. Trigger manual review process
    }
}
