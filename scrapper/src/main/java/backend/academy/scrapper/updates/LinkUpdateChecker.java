package backend.academy.scrapper.updates;

import backend.academy.scrapper.clients.GitHubClient;
import backend.academy.scrapper.model.Link;
import backend.academy.scrapper.repositories.LinkRepository;
import backend.academy.scrapper.repositories.UpdateRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.Map;
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

    public void checkForUpdates() {

        Set<Link> links = linkRepository.getAllLinks();

        for (Link link: links) {

            JsonNode response = gitHubClient.getApi(link.url());
            log.warn("Response: " + response.toString());



        }


    }

}
