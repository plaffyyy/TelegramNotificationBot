package backend.academy.scrapper.services.updateSend;

import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.model.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.message-transport", havingValue = "Kafka")
public class SendNotificationKafka implements SendNotification{

    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final Random random = new Random();


    @Value("${app.kafka.topic.updates}")
    private String updatesTopic;

    @Override
    public void sendUpdateToBot(Link link, List<Long> ids, String description) {
        LinkUpdateRequest request = new LinkUpdateRequest(
            random.nextLong(),
            link.url(),
            description,
            ids
        );

        kafkaTemplate.send(updatesTopic, request);
    }
}
