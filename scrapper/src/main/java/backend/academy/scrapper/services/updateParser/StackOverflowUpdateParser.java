package backend.academy.scrapper.services.updateParser;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class StackOverflowUpdateParser extends UpdateParser {
    @Override
    public String parse(ObjectNode response, ObjectNode lastUpdate) {
        return "";
    }
}
