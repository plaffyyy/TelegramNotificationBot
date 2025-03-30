package backend.academy.scrapper.services.updateSend;

import backend.academy.scrapper.entities.Link;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UpdateRequestService implements SendNotification {

    public UpdateRequestService(@Value("${url.updates}") String botUpdates) {
        this.botUpdates = botUpdates;
    }

    private final RestClient restClient = RestClient.builder().build();
    private final Random random = new Random();
    private final String botUpdates;

    public void sendUpdateToBot(Link link, List<Long> ids, String description) {

        Map<String, Object> jsonRequest = Map.of(
                "id", random.nextLong(), "url", link.url(), "description", description, "tgChatIds", ids);

        restClient
                .post()
                .uri(botUpdates)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonRequest)
                .retrieve()
                .toEntity(Void.class);
    }
}
