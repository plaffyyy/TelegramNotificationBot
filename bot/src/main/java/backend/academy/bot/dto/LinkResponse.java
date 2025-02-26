package backend.academy.bot.dto;

import backend.academy.bot.model.Link;
import java.util.Set;

public record LinkResponse(Set<Link> links, int size) {}
