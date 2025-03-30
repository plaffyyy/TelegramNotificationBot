package backend.academy.scrapper.clients;

import backend.academy.scrapper.exceptions.IncorrectLinkException;
import backend.academy.scrapper.services.ClientRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        log.info("questionId: {}", questionId);
        if (questionId.equals(url)) {
            throw new IncorrectLinkException("Incorrect Stack Overflow link");
        }

        String apiLink = "https://api.stackexchange.com/2.3/questions/" + questionId;
        log.warn("Api link: {}", apiLink);


        try {
            String questionResponse = clientRequestService.stackOverflowResponse(apiLink + "/?site=stackoverflow", soToken);
            String answersResponse = clientRequestService.stackOverflowResponse(apiLink + "/answers?order=desc&sort=activity&site=stackoverflow&filter=withbody", soToken);
            String commentsResponse = clientRequestService.stackOverflowResponse(apiLink + "/comments?order=desc&sort=creation&site=stackoverflow&filter=withbody", soToken);

            ObjectNode questionJson = (ObjectNode) objectMapper.readTree(questionResponse);
            JsonNode answersJson = objectMapper.readTree(answersResponse);
            JsonNode commentsJson = objectMapper.readTree(commentsResponse);

            ObjectNode combinedResponse = objectMapper.createObjectNode();
            combinedResponse.set("title", questionJson.path("items").get(0).path("title"));
            combinedResponse.set("answers", answersJson.path("items"));
            combinedResponse.set("comments", commentsJson.path("items"));

            return combinedResponse;

        } catch (RestClientException | JsonProcessingException e) {
            throw new IncorrectLinkException("Incorrect link");
        }
    }
}
