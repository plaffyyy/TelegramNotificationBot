package backend.academy.scrapper.updates;

import backend.academy.scrapper.clients.GitHubClient;
import backend.academy.scrapper.model.Link;
import backend.academy.scrapper.repositories.LinkRepository;
import backend.academy.scrapper.repositories.UpdateRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class LinkUpdateChecker {

    @Autowired
    private final GitHubClient gitHubClient;
    @Autowired
    private final LinkRepository linkRepository;
    @Autowired
    private final UpdateRepository updateRepository;

    private static final String botUpdates = "http://bot:8080/updates";


    public void checkForUpdates() {

        Set<Link> links = linkRepository.getAllLinks();

        for (Link link: links) {

            JsonNode response = gitHubClient.getApi(link.url());

            JsonNode lastUpdate = updateRepository.getLastUpdate(link.url());
            if (lastUpdate == null) {
                updateRepository.addUpdate(link.url(), response);
            }

            if (!response.equals(lastUpdate)) {
                //TODO: http request to bot
                List<Long> ids = linkRepository.getIdsByLink(link);

                sendUpdateToBot(link, ids);

                updateRepository.changeUpdate(link.url(), lastUpdate);


            }


        }


    }
    private void sendUpdateToBot(Link link, List<Long> ids) {
        RestClient restClient = RestClient.builder().build();

        Map<String, Object> jsonRequest = Map.of(
            "id", new Random().nextLong(),
            "url", link.url(),
            "description", "empty description",
            "tgChatIds", ids
        );

        ResponseEntity<UpdateRepository> response = restClient.post()
            .uri(botUpdates)
            .contentType(MediaType.APPLICATION_JSON)
            .body(jsonRequest)
            .retrieve()
            .toEntity(UpdateRepository.class);

        log.info("Update response: " + response.toString());



    }

}
