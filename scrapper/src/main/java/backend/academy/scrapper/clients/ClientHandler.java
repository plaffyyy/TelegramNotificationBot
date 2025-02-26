package backend.academy.scrapper.clients;

import backend.academy.scrapper.exceptions.UndefinedUrlException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public final class ClientHandler {

    private final String gitHubToken;

    public Client handleClients(String url) {

        if (url.contains("git")) {
            return new GitHubClient(gitHubToken);
        } else if (url.contains("stack")) {
            return new StackOverflowClient();
        } else {
            throw new UndefinedUrlException("govno link");
        }
    }
}
