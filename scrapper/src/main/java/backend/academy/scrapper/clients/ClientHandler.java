package backend.academy.scrapper.clients;

import backend.academy.scrapper.exceptions.UndefinedUrlException;
import org.springframework.stereotype.Component;

@Component
public final class ClientHandler {

    public Client handleClients(String url) {

        return switch (url) {

            case "git" -> new GitHubClient();

            case "stack" -> new StackOverflowClient();

            default -> throw new UndefinedUrlException("Unexpected url");
        };

    }

}
