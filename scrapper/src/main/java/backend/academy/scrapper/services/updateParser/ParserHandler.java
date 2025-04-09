package backend.academy.scrapper.services.updateParser;

import backend.academy.scrapper.exceptions.UndefinedUrlException;

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
