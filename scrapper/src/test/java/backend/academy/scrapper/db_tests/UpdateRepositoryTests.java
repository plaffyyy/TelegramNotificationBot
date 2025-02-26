package backend.academy.scrapper.db_tests;

import backend.academy.scrapper.model.Link;
import backend.academy.scrapper.repositories.UpdateRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class UpdateRepositoryTests {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
        "https://github.com/plaffyyy/SpringMVCLearn",
        "https://github.com/plaffyyy"
    })
    public void addAndGetUpdateTest(String url) {
        String jsonString = """
            {
            "info": "empty info"
            }
            """;
        Map<Long, Set<Link>> expectedMap = new HashMap<>();
        JsonNode update = objectMapper.readTree(jsonString);
        UpdateRepository updateRepository = new UpdateRepository();

        updateRepository.addUpdate(url, update);
        JsonNode jsonNode = updateRepository.getLastUpdate(url);


        assertEquals(jsonNode, update);


    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
        "https://github.com/plaffyyy/asdasdasd",
        "https://github.com/plaffyyy"
    })
    public void getLastUpdateFailure(String url) {
        UpdateRepository updateRepository = new UpdateRepository();

        JsonNode update = updateRepository.getLastUpdate(url);

        assertNull(update);


    }


}
