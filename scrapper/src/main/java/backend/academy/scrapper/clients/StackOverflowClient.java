package backend.academy.scrapper.clients;

import backend.academy.scrapper.exceptions.IncorrectLinkException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public non-sealed class StackOverflowClient extends Client {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public JsonNode getApi(String url) {

        RestClient restClient = RestClient.builder().build();

        String questionId = url.replaceFirst("^https://stackoverflow\\.com/questions/(\\d+).*", "$1");

        if (questionId.equals(url)) {
            log.error("Некорректная ссылка на Stack Overflow: " + url);
            throw new IncorrectLinkException("Incorrect Stack Overflow link");
        }

        String apiLink = "https://api.stackexchange.com/2.3/questions/" + questionId
                + "?order=desc&sort=activity&site=stackoverflow";
        log.warn("Api link: " + apiLink);

        try {
            String response = restClient.get().uri(apiLink).retrieve().body(String.class);

            log.warn("stack json " + response);

            return objectMapper.readTree(response);

        } catch (Exception e) {
            log.error("Ошибка при разборе ответа StackOverflow", e);
            throw new IncorrectLinkException("Incorrect link");
        }
    }
}
