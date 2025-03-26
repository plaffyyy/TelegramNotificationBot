package backend.academy.scrapper.dto;

import backend.academy.scrapper.entities.Link;
import java.util.Set;

public record LinkResponse(Set<Link> links, int size) {}
