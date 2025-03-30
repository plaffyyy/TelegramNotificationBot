package backend.academy.scrapper.services.updateParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class StackOverflowUpdateParser extends UpdateParser {
    @Override
    public String parse(ObjectNode response, ObjectNode lastUpdate) {
        StringBuilder result = new StringBuilder();
        JsonNode newAnswers = response.get("answers");
        JsonNode lastAnswers = lastUpdate.get("answers");
        // я беру вариант только с добавлением, поэтому только знак >
        if (newAnswers.size() > lastAnswers.size()) {
            ObjectNode newAnswer = (ObjectNode) newAnswers.get(0);
            createDescription(result, newAnswer, "ответ");
        }

        JsonNode newQuestions = response.get("questions");
        JsonNode lastQuestions = lastUpdate.get("questions");
        if (newQuestions.size() > lastQuestions.size()) {
            ObjectNode newQuestion = (ObjectNode) newQuestions.get(0);
            createDescription(result, newQuestion, "вопрос");
        }
        return result.toString();
    }

    private void createDescription(StringBuilder result, ObjectNode newField, String type) {
        result.append("Новый ").append(type).append(": ").append(getSafeText(newField, "title")).append("\n")
            .append("Пользователь: ").append(getSafeText(newField.path("owner"), "display_name")).append("\n")
            .append("Время создания: ").append(parseTime(newField.path("creation_date").asLong())).append("\n")
            .append("Превью описания: ").append(getSafeText(newField, "body").substring(0, 200)).append("\n");
    }
    private String getSafeText(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null ? field.asText() : "";
    }
}
