package backend.academy.bot.dto;

import java.util.List;

public record LinkUpdateRequest(Long id, String url, String description, List<Long> ids) {}
