package backend.academy.scrapper.clients;

import backend.academy.scrapper.exceptions.UndefinedUrlException;
import backend.academy.scrapper.services.ClientRequestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public final class ClientHandler {

    private final String gitHubToken;
    private final ClientRequestService clientRequestService;

    public Client handleClients(String url) {

        if (url.contains("git")) {
            return new GitHubClient(gitHubToken, clientRequestService);
        } else if (url.contains("stack")) {
            return new StackOverflowClient(clientRequestService);
        } else {
            throw new UndefinedUrlException("Incorrect link");
        }
    }
}
