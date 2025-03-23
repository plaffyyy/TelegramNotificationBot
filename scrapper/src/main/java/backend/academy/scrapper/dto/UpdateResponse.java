package backend.academy.scrapper.dto;

import java.util.List;

public record UpdateResponse(long id, String url, String description, List<Long> ids) {}
