package backend.academy.scrapper.services.updateParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public sealed abstract class UpdateParser permits GitHubUpdateParser, StackOverflowUpdateParser {

    /**
     * Парсит ответ от API и возвращает строку с обновлением
     * @param response ответ от API
     * @param lastUpdate последнее обновление из базы данных
     * @return строка с обновлением
     */
    public abstract String parse(ObjectNode response, ObjectNode lastUpdate);

}
