package backend.academy.scrapper.updates;

import backend.academy.scrapper.clients.Client;
import backend.academy.scrapper.clients.ClientHandler;
import backend.academy.scrapper.exceptions.UndefinedUrlException;
import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.services.updateSend.UpdateRequestService;
import backend.academy.scrapper.services.data.LinkService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@Service
@AllArgsConstructor
public class LinkUpdateChecker {
    private final LinkService linkService;
    private final ClientHandler clientHandler;
    private final UpdateRequestService updateRequestService;

    @SneakyThrows
    public void checkForUpdates() {

        Set<Link> links = linkService.getAllLinks();
        log.info("Links in update checker: {}", links);
        for (Link link : links) {
            try {
                Client client = clientHandler.handleClients(link.url());
                JsonNode response = client.getApi(link.url());
                if (response == null) continue;
                JsonNode lastUpdate = linkService.getUpdate(link.url());

                ObjectMapper objectMapper = new ObjectMapper();
                String responseJson = objectMapper.writeValueAsString(response);
                String lastUpdateJson = objectMapper.writeValueAsString(lastUpdate);
                //изменение когда ссылка только была добавлена
                // и еще нет обновлений
                if (lastUpdateJson.equals("{}")) {
                    linkService.changeUpdate(link.url(), response);

                } else if (!responseJson.equals(lastUpdateJson)) {
                    List<Long> ids = linkService.getIdsByLink(link);

                    updateRequestService.sendUpdateToBot(link, ids);

                    linkService.changeUpdate(link.url(), response);
                }
            } catch (UndefinedUrlException e) {
                log.error("Ошибка в LinkUpdateChecker: {}", e.getMessage());
            }
        }
    }
}
