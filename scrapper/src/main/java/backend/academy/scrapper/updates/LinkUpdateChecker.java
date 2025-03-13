package backend.academy.scrapper.updates;

import backend.academy.scrapper.clients.Client;
import backend.academy.scrapper.clients.ClientHandler;
import backend.academy.scrapper.exceptions.UndefinedUrlException;
import backend.academy.scrapper.model.Link;
import backend.academy.scrapper.repositories.LinkRepository;
import backend.academy.scrapper.repositories.UpdateRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Getter
@Component
@AllArgsConstructor
public class LinkUpdateChecker {
    private final Random random = new Random();

    @Autowired
    private final LinkRepository linkRepository;

    @Autowired
    private final UpdateRepository updateRepository;

    @Autowired
    private final ClientHandler clientHandler;

    @Value("${url.updates}")
    private static String botUpdates;

    @SneakyThrows
    public void checkForUpdates() {

        Set<Link> links = linkRepository.getAllLinks();

        for (Link link : links) {
            try {
                Client client = clientHandler.handleClients(link.url());
                JsonNode response = client.getApi(link.url());
                if (response == null) continue;
                JsonNode lastUpdate = updateRepository.getLastUpdate(link.url());

                ObjectMapper objectMapper = new ObjectMapper();
                String responseJson = objectMapper.writeValueAsString(response);
                String lastUpdateJson = objectMapper.writeValueAsString(lastUpdate);

                if (lastUpdate == null) {
                    updateRepository.addUpdate(link.url(), response);

                } else if (!responseJson.equals(lastUpdateJson)) {
                    List<Long> ids = linkRepository.getIdsByLink(link);

                    sendUpdateToBot(link, ids);

                    updateRepository.changeUpdate(link.url(), response);
                }
            } catch (UndefinedUrlException e) {
                log.error("Ошибка в LinkUpdateChecker: {}", e.getMessage());
            }
        }
    }

    private void sendUpdateToBot(Link link, List<Long> ids) {
        RestClient restClient = RestClient.builder().build();

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
