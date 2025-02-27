package backend.academy.scrapper.clients;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@AllArgsConstructor
@Component
public non-sealed class GitHubClient extends Client {
    @Autowired
    private final String gitHubToken;

    @Override
    public JsonNode getApi(String url) {
        log.info("Token: {}", gitHubToken);

        RestClient restClient = RestClient.builder().build();

        String cleanUrl = url.replaceFirst("^https://github\\.com/", "");
        String apiLink = "https://api.github.com/repos/" + cleanUrl;
        log.warn("Api link: {}", apiLink);

        try {
            ResponseEntity<String> response = restClient
                    .get()
                    .uri(apiLink)
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("Authorization", "Bearer " + gitHubToken)
                    .retrieve()
                    .toEntity(String.class); // Получаем JSON как строку

            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error("Ошибка при разборе ответа GitHub", e);
            return null;
        }
    }
}
