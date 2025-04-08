package backend.academy.scrapper.db_tests;

import backend.academy.scrapper.database_config.DbConfigTest;
import backend.academy.scrapper.entities.Chat;
import backend.academy.scrapper.repositories.ChatRepository;
import backend.academy.scrapper.services.data.OrmLinkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;


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
}
