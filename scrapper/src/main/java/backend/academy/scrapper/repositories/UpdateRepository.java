package backend.academy.scrapper.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Getter
@Repository
public class UpdateRepository {
    // TODO сделать ключ id ссылки, чтобы у разных пользователей было все корректно(более логично просто)
    private final Map<String, JsonNode> urlUpdate;

    public UpdateRepository() {
        urlUpdate = new HashMap<>();
    }

    public JsonNode getLastUpdate(String url) {
        return urlUpdate.getOrDefault(url, null);
    }

    public void addUpdate(String url, JsonNode update) {
        urlUpdate.putIfAbsent(url, update);
    }

    public void changeUpdate(String url, JsonNode update) {
        urlUpdate.replace(url, update);
    }
}
