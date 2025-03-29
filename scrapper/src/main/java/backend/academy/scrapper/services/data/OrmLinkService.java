package backend.academy.scrapper.services.data;

import backend.academy.scrapper.entities.Chat;
import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.exceptions.ChatNotCreatedException;
import backend.academy.scrapper.repositories.ChatRepository;
import backend.academy.scrapper.repositories.LinkRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty(name = "access-type", havingValue = "ORM")
public class OrmLinkService extends LinkService {

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
        Optional<Chat> chat = chatRepository.findById(chatId);
        return linkRepository.getAllByChat(chat.orElseThrow(() ->
            new ChatNotCreatedException("Чат с таким id не существует")));
    }

    //TODO: добавить выброс глобальной ошибки при неправильном
    // или несуществующем chatId
    public void addLink(Long chatId, Link link) {

        log.info("In data service layer");
        Chat chat = chatRepository.findById(chatId).orElseThrow(
            () -> new ChatNotCreatedException("Нет чата с таким id")
        );

        log.info("Chat: {}", chat);
        link.chat(chat);
        log.info("Link for save: {}", link);
        linkRepository.save(link);
    }

    public Link removeLinkByUrl(long chatId, String url) {

        List<Link> links = linkRepository.findAllByUrl(url);
        for (Link link : links) {
            if (link.chat() != null && link.chat().id().equals(chatId)) {
                linkRepository.delete(link); // Сохраняем обновленную ссылку без этого чата
                return link;
            }
        }
        return null;

    }

    /**
     * Здесь получаем все chatId по url, для того, чтобы им потом уведомление присылать
     * @param link передаем параметр link, но берем из не url потом
     * @return List<Long> - лист всех id чатов
     */
    public List<Long> getIdsByLink(Link link) {
        return linkRepository.findAllByUrl(link.url())
            .stream()
            .map(l -> l.chat().id())
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    public JsonNode getUpdate(String url) {
        return linkRepository.findAllByUrl(url)
            .stream()
            .findFirst()
            .map(Link::update)
            .orElse(null);
    }

    @Override
    public void changeUpdate(String url, JsonNode update) {
        linkRepository.findAllByUrl(url)
            .forEach(link -> {
                link.update(update);
                linkRepository.save(link);
            });
    }


}
