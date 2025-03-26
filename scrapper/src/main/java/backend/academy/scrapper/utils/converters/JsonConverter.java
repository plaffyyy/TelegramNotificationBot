package backend.academy.scrapper.utils.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter(autoApply = true)
public class JsonConverter implements AttributeConverter<JsonNode, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode jsonNode) {
        try {
            return jsonNode != null ? objectMapper.writeValueAsString(jsonNode) : "{}";
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сериализации JsonNode", e);
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String json) {
        try {
            return json != null ? objectMapper.readTree(json) : null;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка десериализации JsonNode", e);
        }
    }
}
