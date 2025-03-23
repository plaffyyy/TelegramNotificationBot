package backend.academy.scrapper.repositories;

import backend.academy.scrapper.model.Link;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Getter
@Repository
public class LinkRepository {

    private final Map<Long, Set<Link>> userLink;

    public LinkRepository() {
        userLink = new HashMap<>();
    }

    public Set<Link> getAllLinks() {
        return userLink.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public void createChatById(Long id) {
        userLink.put(id, new HashSet<>());
    }

    public void deleteChatById(Long id) {
        userLink.remove(id);
    }

    public Set<Link> getLinksByChatId(Long chatId) {
        return userLink.get(chatId);
    }

    public void addLink(Long chatId, Link link) {

        userLink.get(chatId).add(link);
        log.warn("user link: {}", userLink.toString());
    }

    public Link removeLinkByUrl(long chatId, String url) {

        Set<Link> linksByChatId = getLinksByChatId(chatId);
        boolean hasLink = false;
        for (Link link : linksByChatId) {
            if (link.url().equals(url)) {
                hasLink = true;
                break;
            }
        }
        if (!hasLink) return null;

        Set<Link> links = userLink.remove(chatId);
        for (Link link : links) {
            if (link.url().equals(url)) {
                links.remove(link);
                userLink.put(chatId, links);
                return link;
            }
        }
        return null;
    }

    public List<Long> getIdsByLink(Link link) {
        List<Long> ids = new ArrayList<>();
        userLink.forEach((k, v) -> {
            for (Link tempLink : v) {
                if (tempLink.url().equals(link.url())) {
                    ids.add(k);
                    break;
                }
            }
        });
        log.info("Id of chats: {}", ids);
        return ids;
    }
}
