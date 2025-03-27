package backend.academy.scrapper.services.data;

import backend.academy.scrapper.entities.Link;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Service
@ConditionalOnProperty(name = "access-type", havingValue = "SQL")
public class SqlLinkService extends LinkService {

    @Override
    public Set<Link> getAllLinks() {
        return Set.of();
    }

    @Override
    public void createChatById(Long id) {

    }

    @Override
    public void deleteChatById(Long id) {

    }

    @Override
    public Set<Link> getLinksByChatId(Long chatId) {
        return Set.of();
    }

    @Override
    public void addLink(Long chatId, Link link) {

    }

    @Override
    public Link removeLinkByUrl(long chatId, String url) {
        return null;
    }

    @Override
    public List<Long> getIdsByLink(Link link) {
        return List.of();
    }
}
