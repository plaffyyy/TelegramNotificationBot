package backend.academy.scrapper.services.data;

import backend.academy.scrapper.entities.Chat;
import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.exceptions.ChatNotCreatedException;
import backend.academy.scrapper.repositories.ChatRepository;
import backend.academy.scrapper.repositories.LinkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrmLinkService implements LinkService {

    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;

    public Set<Link> getAllLinks() {
        return new HashSet<>(linkRepository.findAll());
    }

    public void createChatById(Long id) {
        chatRepository.save(new Chat(id));
    }

    public void deleteChatById(Long id) {
        chatRepository.deleteById(id);
    }

    public Set<Link> getLinksByChatId(Long chatId) {
        return linkRepository.getAllByChats(List.of(new Chat(chatId)));
    }

    public void addLink(Long chatId, Link link) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(
            () -> new ChatNotCreatedException("Нет чата с таким id")
        );
        link.chats().add(chat);
        linkRepository.save(link);
    }

    public Link removeLinkByUrl(long chatId, String url) {

        List<Link> links = linkRepository.findAllByUrl(url);
        for (Link link : links) {
            if (link.chats().removeIf(chat -> chat.id().equals(chatId))) {
                linkRepository.save(link); // Сохраняем обновленную ссылку без этого чата
                return link;
            }
        }
        return null;


    }

    public List<Long> getIdsByLink(Link link) {
        return linkRepository.findAllByUrl(link.url())
            .stream()
            .flatMap(l -> l.chats().stream().map(Chat::id))
            .distinct()
            .collect(Collectors.toList());
    }

}
