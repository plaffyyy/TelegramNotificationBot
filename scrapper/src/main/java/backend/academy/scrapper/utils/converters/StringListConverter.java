package backend.academy.scrapper.utils.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

/**
 * Converter class для того, чтобы в базе данных хранить строку,
 * сделанную из листа строк. И также парсить в обратную сторону
 */
@Converter(autoApply = true)
public final class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        try {
            return (list != null) ? objectMapper.writeValueAsString(list) : "[]";
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации List<String> в JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String json) {
        try {
            return (json != null && !json.isEmpty()) ? objectMapper.readValue(json, List.class) : List.of();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка десериализации JSON в List<String>", e);
        }
    }
}
