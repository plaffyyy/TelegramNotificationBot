package backend.academy.scrapper.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;


@Slf4j
@Service
public class LinkService {

    private Map<Long, Set<String>> userLink;

    public LinkService() {
        userLink = new HashMap<>();
    }

    public Set<String> getLinksByChatId(Long chatId) {
        return userLink.getOrDefault(chatId, new HashSet<>());
    }

    public void track(long chatId, String url) {

        userLink.computeIfAbsent(chatId, k -> new HashSet<>()).add(url);

        log.debug(userLink.toString());


    }

}
