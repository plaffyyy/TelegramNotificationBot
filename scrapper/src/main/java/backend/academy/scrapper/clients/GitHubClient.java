package backend.academy.scrapper.clients;

import backend.academy.scrapper.services.ClientRequestService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public non-sealed class GitHubClient extends Client {


    @Autowired
    public GitHubClient(String gitHubToken, ClientRequestService clientRequestService) {
        super(clientRequestService, gitHubToken);
    }

    @Override
    public JsonNode getApi(String url) {
        log.info("Token: {}", gitHubToken);

        String cleanUrl = url.replaceFirst("^https://github\\.com/", "");
        String apiLink = "https://api.github.com/repos/" + cleanUrl;
        log.warn("Api link: {}", apiLink);
        String apiPullsLink = apiLink + "/pulls";
        String apiIssuesLink = apiLink + "/issues";

        try {
            ResponseEntity<String> response1 =
                clientRequestService.gitHubResponse(apiPullsLink, gitHubToken); // Получаем JSON как строку
            ResponseEntity<String> response2 =
                clientRequestService.gitHubResponse(apiIssuesLink, gitHubToken);

            JsonNode pulls = objectMapper.readTree(response1.getBody());
            JsonNode issues = objectMapper.readTree(response2.getBody());

            ObjectNode combinedResponse = objectMapper.createObjectNode();
            combinedResponse.set("pulls", pulls);
            combinedResponse.set("issues", issues);

            return combinedResponse;
        } catch (Exception e) {
            log.error("Ошибка при разборе ответа GitHub", e);
            return null;
        }
    }
}
