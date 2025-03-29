package backend.academy.scrapper.dto;

import backend.academy.scrapper.model.Link;
import java.util.Set;

public record LinkResponse(Set<Link> links, int size) {}
