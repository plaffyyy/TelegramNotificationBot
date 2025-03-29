package backend.academy.scrapper.services.updateParser;

import backend.academy.scrapper.clients.GitHubClient;
import backend.academy.scrapper.clients.StackOverflowClient;
import backend.academy.scrapper.exceptions.UndefinedUrlException;
import com.fasterxml.jackson.databind.JsonNode;

public class ParserHandler {

    public UpdateParser handleClients(String url) {
        if (url.contains("git")) {
            return new GitHubUpdateParser();
        } else if (url.contains("stack")) {
            return new StackOverflowUpdateParser();
        } else {
            throw new UndefinedUrlException("Incorrect link");
        }
    }

}
