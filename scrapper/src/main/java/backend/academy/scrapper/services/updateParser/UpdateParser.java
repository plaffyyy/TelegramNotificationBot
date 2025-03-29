package backend.academy.scrapper.services.updateParser;

import com.fasterxml.jackson.databind.JsonNode;

public sealed abstract class UpdateParser permits GitHubUpdateParser, StackOverflowUpdateParser {

    public abstract String parse(JsonNode response, JsonNode lastUpdate);

}
