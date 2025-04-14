package backend.academy.scrapper.clients;

import backend.academy.scrapper.services.ClientRequestService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract sealed class Client permits GitHubClient, StackOverflowClient {

    public Client(ClientRequestService clientRequestService, String gitHubToken) {
        this.clientRequestService = clientRequestService;
        this.gitHubToken = gitHubToken;
        this.soToken = "";
    }

    public Client(String soToken, ClientRequestService clientRequestService) {
        this.soToken = soToken;
        this.clientRequestService = clientRequestService;
        this.gitHubToken = "";
    }

    protected final String gitHubToken;
    protected final ClientRequestService clientRequestService;
    protected final String soToken;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    public abstract JsonNode getApi(String url);
}
