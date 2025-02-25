package backend.academy.scrapper.clients;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Slf4j
@Component
public final class GitHubClient {

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON парсер

    public JsonNode getApi(String url) {
        RestClient restClient = RestClient.builder().build();

        String apiLink = "https://api.github.com/repos/" + url;

        ResponseEntity<String> response = restClient.get()
            .uri(apiLink)
            .header("Accept", "application/vnd.github.v3+json")
            .retrieve()
            .toEntity(String.class);  // Получаем JSON как строку

        log.warn("Response from GitHub: " + response.getBody());

        try {
            return objectMapper.readTree(response.getBody()); // Конвертируем JSON в объект
        } catch (Exception e) {
            log.error("Ошибка при разборе ответа GitHub", e);
            return null;
        }
    }


}
