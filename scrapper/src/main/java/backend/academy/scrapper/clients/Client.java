package backend.academy.scrapper.clients;

import backend.academy.scrapper.services.ClientRequestService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract sealed class Client permits GitHubClient, StackOverflowClient {

    protected String gitHubToken;
    protected final ClientRequestService clientRequestService;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    public abstract JsonNode getApi(String url);
}
