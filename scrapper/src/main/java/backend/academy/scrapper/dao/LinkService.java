package backend.academy.scrapper.dao;

import backend.academy.scrapper.model.Link;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;


@Slf4j
@Service
public class LinkService {

    private Map<Long, Set<Link>> userLink;

    public LinkService() {
        userLink = new HashMap<>();
    }

    public void createChatById(long id) {
        userLink.put(id, new HashSet<>());
    }
    public void deleteChatById(long id) {
        userLink.remove(id);
    }

    public Set<Link> getLinksByChatId(Long chatId) {
        return userLink.get(chatId);
    }

    public void addLink(long chatId, Link link) {

        userLink.get(chatId).add(link);
        log.warn(userLink.toString());

    }

    public Link removeLinkByUrl(long chatId, String url) {

        Set<Link> links = userLink.remove(chatId);
        for (Link link: links) {
            if (link.url().equals(url)) {
                links.remove(link);
                userLink.put(chatId, links);
                return link;
            }
        }
        return null;
    }

}
