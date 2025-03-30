package backend.academy.scrapper.services.updateParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public sealed abstract class UpdateParser permits GitHubUpdateParser, StackOverflowUpdateParser {

    /**
     * Парсит ответ от API и возвращает строку с обновлением
     * @param response ответ от API
     * @param lastUpdate последнее обновление из базы данных
     * @return строка с обновлением
     */
    public abstract String parse(ObjectNode response, ObjectNode lastUpdate);

    protected String parseTime(long timestamp) {
        return Instant.ofEpochSecond(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
