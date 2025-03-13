package backend.academy.scrapper.services;

import backend.academy.scrapper.model.Link;
import backend.academy.scrapper.repositories.UpdateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class UpdateRequestService {

    public UpdateRequestService(@Value("${url.updates}") String botUpdates) {
        this.botUpdates = botUpdates;
    }
    private final RestClient restClient = RestClient.builder().build();
    private final Random random = new Random();
    private final String botUpdates;

    public void sendUpdateToBot(Link link, List<Long> ids) {

        Map<String, Object> jsonRequest = Map.of(
            "id", random.nextLong(), "url", link.url(), "description", "empty description", "tgChatIds", ids);

        restClient
            .post()
            .uri(botUpdates)
            .contentType(MediaType.APPLICATION_JSON)
            .body(jsonRequest)
            .retrieve()
            .toEntity(UpdateRepository.class);
    }

}
