package backend.academy.scrapper.services.data;

import backend.academy.scrapper.entities.Chat;
import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.exceptions.ChatNotCreatedException;
import backend.academy.scrapper.repositories.ChatRepository;
import backend.academy.scrapper.repositories.LinkRepository;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@ConditionalOnProperty(name = "access-type", havingValue = "ORM")
public class OrmLinkService extends LinkService {

    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;

    public Set<Link> getAllLinks() {
        return new HashSet<>(linkRepository.findAll());
    }

    @Transactional
    public void createChatById(Long id) {
        chatRepository.save(new Chat(id));
        chatRepository.flush();
    }

    @Transactional
    public void deleteChatById(Long id) {
        chatRepository.deleteById(id);
        chatRepository.flush();
    }

    /**
     * Получение всех чатов из базы данных для теста
     *
     * @return количество чатов
     */
    public long getAllChats() {
        return chatRepository.count();
    }

    public Set<Link> getLinksByChatId(Long chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        return linkRepository.getAllByChat(
                chat.orElseThrow(() -> new ChatNotCreatedException("Чат с таким id не существует")));
    }

    @Override
    public Set<Link> getLinksByChatIdAndTag(Long chatId, String tag) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        return linkRepository
                .getAllByChat(chat.orElseThrow(() -> new ChatNotCreatedException("Чат с таким id не существует")))
                .stream()
                .filter(link -> link.tags().contains(tag))
                .collect(Collectors.toSet());
    }

    // TODO: добавить выброс глобальной ошибки при неправильном
    // или несуществующем chatId
    @Transactional
    public void addLink(Long chatId, Link link) {

        log.info("In data service layer");
        Chat chat =
                chatRepository.findById(chatId).orElseThrow(() -> new ChatNotCreatedException("Нет чата с таким id"));

        log.info("Chat: {}", chat);
        link.chat(chat);
        log.info("Link for save: {}", link);
        linkRepository.save(link);
        linkRepository.flush();
    }

    @Transactional
    public Link removeLinkByUrl(long chatId, String url) {

        List<Link> links = linkRepository.findAllByUrl(url);
        for (Link link : links) {
            if (link.chat() != null && link.chat().id().equals(chatId)) {
                linkRepository.delete(link); // Сохраняем обновленную ссылку без этого чата
                linkRepository.flush();
                return link;
            }
        }
        return null;
    }

    /**
     * Здесь получаем все chatId по url, для того, чтобы им потом уведомление присылать
     *
     * @param link передаем параметр link, но берем из не url потом
     * @return List<Long> - лист всех id чатов
     */
    public List<Long> getIdsByLink(Link link) {
        return linkRepository.findAllByUrl(link.url()).stream()
                .map(l -> l.chat().id())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public JsonNode getUpdate(String url) {
        return linkRepository.findAllByUrl(url).stream()
                .findFirst()
                .map(Link::update)
                .orElse(null);
    }

    @Transactional
    @Override
    public void changeUpdate(String url, JsonNode update) {
        linkRepository.findAllByUrl(url).forEach(link -> {
            link.update(update);
            linkRepository.save(link);
            linkRepository.flush();
        });
    }
}
