package backend.academy.scrapper.updates;

import backend.academy.scrapper.clients.Client;
import backend.academy.scrapper.clients.ClientHandler;
import backend.academy.scrapper.exceptions.UndefinedUrlException;
import backend.academy.scrapper.model.Link;
import backend.academy.scrapper.repositories.LinkRepository;
import backend.academy.scrapper.repositories.UpdateRepository;
import backend.academy.scrapper.services.UpdateRequestService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
@AllArgsConstructor
public class LinkUpdateChecker {
    private final LinkRepository linkRepository;
    private final UpdateRepository updateRepository;
    private final ClientHandler clientHandler;
    private final UpdateRequestService updateRequestService;

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

                    updateRequestService.sendUpdateToBot(link, ids);

                    updateRepository.changeUpdate(link.url(), response);
                }
            } catch (UndefinedUrlException e) {
                log.error("Ошибка в LinkUpdateChecker: {}", e.getMessage());
            }
        }
    }
}
