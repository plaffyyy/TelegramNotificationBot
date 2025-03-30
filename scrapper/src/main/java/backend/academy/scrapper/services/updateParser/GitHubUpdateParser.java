package backend.academy.scrapper.services.updateParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class GitHubUpdateParser extends UpdateParser {

    @Override
    public String parse(ObjectNode response, ObjectNode lastUpdate) {
        StringBuilder result = new StringBuilder();
        JsonNode newPulls = response.get("pulls");
        JsonNode lastPulls = lastUpdate.get("pulls");
        // я беру вариант только с добавлением, поэтому только знак >
        if (newPulls.size() > lastPulls.size()) {
            JsonNode newPull = newPulls.get(0);
            result.append("Новый пул реквест:\n")
                .append("Название: ").append(newPull.get("title").asText()).append("\n")
                .append("Пользователь: ").append(newPull.get("user").get("login").asText()).append("\n")
                .append("Время создания: ").append(newPull.get("created_at").asText()).append("\n")
                .append("Превью описания: ").append(newPull.get("body").asText().substring(0, 200)).append("\n");
        }

        // Check for new issues
        // я беру вариант только с добавлением, поэтому только знак >
        JsonNode newIssues = response.get("issues");
        JsonNode lastIssues = lastUpdate.get("issues");
        if (newIssues.size() > lastIssues.size()) {
            JsonNode newIssue = newIssues.get(0);
            result.append("Новое Issue:\n")
                .append("Название: ").append(newIssue.get("title").asText()).append("\n")
                .append("Пользователь: ").append(newIssue.get("user").get("login").asText()).append("\n")
                .append("Время создания: ").append(newIssue.get("created_at").asText()).append("\n")
                .append("Превью описания: ").append(newIssue.get("body").asText().substring(0, 200)).append("\n");
        }

        return result.toString();
    }
}
