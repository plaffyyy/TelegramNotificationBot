package backend.academy.scrapper.db_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import backend.academy.scrapper.database_config.DbConfigTest;
import backend.academy.scrapper.entities.Chat;
import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.services.data.OrmLinkService;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"spring.test.database.replace=none"})
public class OrmLinkServiceTests extends DbConfigTest {

    @Autowired
    public OrmLinkServiceTests(OrmLinkService ormLinkService) {
        this.ormLinkService = ormLinkService;
    }

    private final OrmLinkService ormLinkService;

    @DisplayName("Проверка, что корректно добавляется и удаляется чат")
    @Test
    public void testCreateAndDeleteChat() {
        ormLinkService.createChatById(1L);
        long chatCount = ormLinkService.getAllChats();

        assertEquals(1, chatCount);

        ormLinkService.createChatById(2L);
        chatCount = ormLinkService.getAllChats();
        assertEquals(2, chatCount);

        ormLinkService.deleteChatById(1L);
        chatCount = ormLinkService.getAllChats();
        assertEquals(1, chatCount);
    }

    @DisplayName("Проверка, что корректно добавляется и удаляется ссылка, а также верно отображаются фильтры")
    @Test
    public void testAddGetAndRemoveLink() {

        Chat chat = new Chat(1L);
        Link link = new Link(
                null,
                "https://github.com/plaffyyy/SpringMVCLearn",
                List.of("first"),
                List.of("first", "second"),
                null,
                chat);

        ormLinkService.createChatById(chat.id());
        ormLinkService.addLink(1L, link);

        Set<Link> links = ormLinkService.getLinksByChatId(chat.id());
        assertNotNull(links);
        assertEquals(1, links.size());
        assertEquals(link.url(), links.iterator().next().url());
        assertEquals(link.filters(), links.iterator().next().filters());

        ormLinkService.removeLinkByUrl(chat.id(), link.url());
        links = ormLinkService.getLinksByChatId(chat.id());
        assertNotNull(links);
        assertEquals(0, links.size());
    }

    @DisplayName("Проверка, что корректно отображаются ссылки по тегу")
    @Test
    public void getLinksByTag() {

        Chat chat = new Chat(1L);
        String firstTag = "first-tag";
        String secondTag = "second-tag";
        Link link1 = new Link(
                null,
                "https://github.com/plaffyyy/SpringMVCLearn1",
                List.of(firstTag, secondTag),
                List.of("first", "second"),
                null,
                chat);
        Link link2 = new Link(
                null,
                "https://github.com/plaffyyy/SpringMVCLearn2",
                List.of(secondTag),
                List.of("first", "second"),
                null,
                chat);

        ormLinkService.createChatById(chat.id());
        ormLinkService.addLink(chat.id(), link1);
        ormLinkService.addLink(chat.id(), link2);

        Set<Link> linksByFirstTag = ormLinkService.getLinksByChatIdAndTag(chat.id(), firstTag);
        assertEquals(1, linksByFirstTag.size());
        assertEquals(link1.url(), linksByFirstTag.iterator().next().url());
    }
}
