package backend.academy.scrapper.db_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import backend.academy.scrapper.db_tests.*;
import backend.academy.scrapper.entities.Chat;
import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.services.data.SqlLinkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Testcontainers
@Transactional
@TestPropertySource(properties = {
    "access-type=SQL" 
})
public class SqlLinkServiceTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
        .withDatabaseName("testDb");

    @Autowired
    public SqlLinkServiceTests(SqlLinkService sqlLinkService, JdbcTemplate jdbcTemplate) {
        this.sqlLinkService = sqlLinkService;
        this.jdbcTemplate = jdbcTemplate;
    }

    private final SqlLinkService sqlLinkService;
    private final JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @DisplayName("Проверка, что корректно добавляется и удаляется чат")
    @Test
    public void testCreateAndDeleteChat() {
        sqlLinkService.createChatById(1L);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat WHERE id = ?", Integer.class, 1L);
        assertNotNull(count);
        assertEquals(1, count);

        sqlLinkService.deleteChatById(1L);
        count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat WHERE id = ?", Integer.class, 1L);
        assertNotNull(count);
        assertEquals(0, count);
        sqlLinkService.deleteChatById(2L);
    }

    @DisplayName("Проверка, что корректно добавляется и удаляется ссылка, а также верно отображаются фильтры")
    @Test
    public void testAddGetAndRemoveLink() {

        Chat chat = new Chat(1L);
        Link link = new Link(
                1L, "https://github.com/plaffyyy/SpringMVCLearn", null, List.of("first", "second"), null, chat);

        sqlLinkService.createChatById(chat.id());
        sqlLinkService.addLink(1L, link);

        Set<Link> links = sqlLinkService.getLinksByChatId(chat.id());
        assertNotNull(links);
        assertEquals(1, links.size());
        assertEquals(link.url(), links.iterator().next().url());
        assertEquals(link.filters(), links.iterator().next().filters());

        sqlLinkService.removeLinkByUrl(chat.id(), link.url());
        links = sqlLinkService.getLinksByChatId(chat.id());
        assertNotNull(links);
        assertEquals(0, links.size());
        sqlLinkService.deleteChatById(chat.id());
    }

    @DisplayName("Проверка, что корректно отображаются ссылки по тегу")
    @Test
    public void getLinksByTag() {

        Chat chat = new Chat(1L);
        String firstTag = "first-tag";
        String secondTag = "second-tag";
        Link link1 = new Link(
                1L,
                "https://github.com/plaffyyy/SpringMVCLearn1",
                List.of(firstTag, secondTag),
                List.of("first", "second"),
                null,
                chat);
        Link link2 = new Link(
                2L,
                "https://github.com/plaffyyy/SpringMVCLearn2",
                List.of(secondTag),
                List.of("first", "second"),
                null,
                chat);

        sqlLinkService.createChatById(chat.id());
        sqlLinkService.addLink(chat.id(), link1);
        sqlLinkService.addLink(chat.id(), link2);

        Set<Link> linksByFirstTag = sqlLinkService.getLinksByChatIdAndTag(chat.id(), firstTag);
        assertEquals(1, linksByFirstTag.size());
        assertEquals(link1.url(), linksByFirstTag.iterator().next().url());
        sqlLinkService.removeLinkByUrl(chat.id(), link1.url());
        sqlLinkService.removeLinkByUrl(chat.id(), link2.url());
        sqlLinkService.deleteChatById(chat.id());
    }
}
