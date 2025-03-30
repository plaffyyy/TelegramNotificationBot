package backend.academy.scrapper.services.updateParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public final class StackOverflowUpdateParser extends UpdateParser {
    @Override
    public String parse(ObjectNode response, ObjectNode lastUpdate) {
        StringBuilder result = new StringBuilder();
        JsonNode newAnswers = response.get("answers");
        JsonNode lastAnswers = lastUpdate.get("answers");
        // я беру вариант только с добавлением, поэтому только знак >
        if (newAnswers.size() > lastAnswers.size()) {
            JsonNode newAnswer = newAnswers.get(0);
            createDescription(result, newAnswer);
        }

        JsonNode newQuestions = response.get("questions");
        JsonNode lastQuestions = lastUpdate.get("questions");
        if (newQuestions.size() > lastQuestions.size()) {
            JsonNode newQuestion = newQuestions.get(0);
            createDescription(result, newQuestion);
        }
        return result.toString();
    }

    private void createDescription(StringBuilder result, JsonNode newField) {
        result.append("Новый ответ в вопросе: ").append(newField.get("title").asText()).append("\n")
            .append("Пользователь: ").append(newField.get("owner").get("display_name").asText()).append("\n")
            .append("Время создания: ").append(parseTime(newField.get("creation_date").asLong())).append("\n")
            .append("Превью описания: ").append(newField.get("body").asText().substring(0, 200)).append("\n");
    }

}
