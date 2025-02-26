package backend.academy.scrapper.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public sealed abstract class Client
    permits GitHubClient, StackOverflowClient {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    public abstract JsonNode getApi(String url);

}
