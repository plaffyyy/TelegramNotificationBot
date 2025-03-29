package backend.academy.bot.model;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public record Link(
    long id,
    String url,
    List<String> tags,
    List<String> filters,
    JsonNode update,
    Chat chat
) {
}
