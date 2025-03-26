package backend.academy.scrapper.services.data;

import backend.academy.scrapper.entities.Link;
import java.util.List;
import java.util.Set;


public abstract class LinkService {

    public abstract Set<Link> getAllLinks();
    public abstract void createChatById(Long id);
    public abstract void deleteChatById(Long id);
    public abstract Set<Link> getLinksByChatId(Long chatId);
    public abstract void addLink(Long chatId, Link link);
    public abstract Link removeLinkByUrl(long chatId, String url);
    public abstract List<Long> getIdsByLink(Link link);

}
