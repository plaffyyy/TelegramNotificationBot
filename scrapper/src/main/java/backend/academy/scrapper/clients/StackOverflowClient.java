package backend.academy.scrapper.clients;

import backend.academy.scrapper.exceptions.IncorrectLinkException;
import backend.academy.scrapper.services.ClientRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
public non-sealed class StackOverflowClient extends Client {

    @Autowired
    public StackOverflowClient(ClientRequestService clientRequestService, String soToken) {
        super(soToken, clientRequestService);
    }

    @Override
    public JsonNode getApi(String url) {

        String questionId = url.replaceFirst("^https://stackoverflow\\.com/questions/(\\d+).*", "$1");

        if (questionId.equals(url)) {
            throw new IncorrectLinkException("Incorrect Stack Overflow link");
        }

        String apiLink = "https://api.stackexchange.com/2.3/questions/" + questionId
                + "?order=desc&sort=activity&site=stackoverflow";
        log.warn("Api link: {}", apiLink);

        try {
            String response = clientRequestService.stackOverflowResponse(apiLink);

            log.warn("stack json {}", response);

            return objectMapper.readTree(response);

        } catch (RestClientException | JsonProcessingException e) {
            throw new IncorrectLinkException("Incorrect link");
        }
    }
}
