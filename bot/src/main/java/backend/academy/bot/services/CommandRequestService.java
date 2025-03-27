package backend.academy.bot.services;

import backend.academy.bot.dto.LinkResponse;
import backend.academy.bot.dto.TrackLinkResponse;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public final class CommandRequestService {
    private final String urlForApi;
    private final String urlForChatApi;

    public CommandRequestService(
            @Value("${url.links}") String urlForApi, @Value("${url.tg-chats}") String urlForChatApi) {
        this.urlForApi = urlForApi;
        this.urlForChatApi = urlForChatApi;
    }

    private final RestClient restClient = RestClient.builder().build();
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<TrackLinkResponse> trackCommandResponse(Map<String, Object> jsonRequest, Long chatId) {
        log.info("urlForApi: {}", urlForApi);
        log.info("chatId in sender: {}", chatId);
        return restClient
                .post()
                .uri(urlForApi)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .body(jsonRequest)
                .retrieve()
                .toEntity(TrackLinkResponse.class);
    }

    public ResponseEntity<LinkResponse> listCommandResponse(Long chatId) {
        return restClient
                .get()
                .uri(urlForApi)
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .retrieve()
                .toEntity(LinkResponse.class);
    }

    public ResponseEntity<Void> startCommandResponse(Long chatId) {
        return restClient
                .post()
                .uri(urlForChatApi + "/{chatId}", chatId)
                .retrieve()
                .toBodilessEntity();
    }

    public ResponseEntity<TrackLinkResponse> untrackCommandResponse(HttpEntity<Map<String, String>> requestEntity) {
        return restTemplate.exchange(urlForApi, HttpMethod.DELETE, requestEntity, TrackLinkResponse.class);
    }
}
