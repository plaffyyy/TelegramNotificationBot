package backend.academy.scrapper.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public record LinkDto(Long id, String url, List<String> tags, List<String> filters, JsonNode update, ChatDto chat) {}
