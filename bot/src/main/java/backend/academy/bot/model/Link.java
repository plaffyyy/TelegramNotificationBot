package backend.academy.bot.model;

import java.util.List;

public record Link(String url, List<String> tags, List<String> filters) {

}
