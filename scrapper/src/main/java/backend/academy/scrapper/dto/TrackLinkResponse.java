package backend.academy.scrapper.dto;

import java.util.List;

public record TrackLinkResponse(long id, String url, List<String> tags, List<String> filters) {}
