package backend.academy.scrapper.clients;

import backend.academy.scrapper.services.ClientRequestService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public non-sealed class GitHubClient extends Client {

    private String gitHubToken;

    public GitHubClient(String gitHubToken, ClientRequestService clientRequestService) {
        super(gitHubToken, clientRequestService);
    }

    @Override
    public JsonNode getApi(String url) {
        log.info("Token: {}", gitHubToken);

        String cleanUrl = url.replaceFirst("^https://github\\.com/", "");
        String apiLink = "https://api.github.com/repos/" + cleanUrl;
        log.warn("Api link: {}", apiLink);

        try {
            ResponseEntity<String> response =
                    clientRequestService.gitHubResponse(apiLink, gitHubToken); // Получаем JSON как строку

            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error("Ошибка при разборе ответа GitHub", e);
            return null;
        }
    }
}
