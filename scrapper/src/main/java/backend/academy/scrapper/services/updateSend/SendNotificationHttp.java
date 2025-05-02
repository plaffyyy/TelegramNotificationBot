package backend.academy.scrapper.services.updateSend;

import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.model.LinkUpdateRequest;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@ConditionalOnProperty(name = "app.message-transport", havingValue = "HTTP")
public class SendNotificationHttp implements SendNotification {

    public SendNotificationHttp(@Value("${url.updates}") String botUpdates) {
        this.botUpdates = botUpdates;
    }
    // TODO: now notification id is a random value, need to change it
    private final RestClient restClient = RestClient.builder().build();
    private final Random random = new Random();
    private final String botUpdates;

    @Override
    public void sendUpdateToBot(Link link, List<Long> ids, String description) {

        LinkUpdateRequest request = new LinkUpdateRequest(random.nextLong(), link.url(), description, ids);
        restClient
                .post()
                .uri(botUpdates)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(Void.class);
    }
}
