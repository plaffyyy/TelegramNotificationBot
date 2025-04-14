package backend.academy.scrapper.dto;

import backend.academy.scrapper.model.LinkDto;
import java.util.Set;

public record LinkResponse(Set<LinkDto> links, int size) {}
