package backend.academy.scrapper.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public final class ClientRequestService {

    private final RestClient restClient = RestClient.builder().build();

    public String stackOverflowResponse(String apiLink) {
        return restClient.get().uri(apiLink).retrieve().body(String.class);
    }

    public ResponseEntity<String> gitHubResponse(String apiLink, String gitHubToken) {
        return restClient
                .get()
                .uri(apiLink)
                .header("Accept", "application/vnd.github.v3+json")
                .header("Authorization", "Bearer " + gitHubToken)
                .retrieve()
                .toEntity(String.class); // Получаем JSON как строку
    }
}
