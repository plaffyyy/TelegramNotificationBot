package backend.academy.scrapper.db_tests;

import backend.academy.scrapper.database_config.DbConfigTest;
import backend.academy.scrapper.entities.Chat;
import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.services.data.OrmLinkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestPropertySource(properties = {
    "spring.test.database.replace=none"
})
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
        Link link = new Link(null, "https://github.com/plaffyyy/SpringMVCLearn",
            List.of("first"), List.of("first", "second"), null, chat);

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


}
