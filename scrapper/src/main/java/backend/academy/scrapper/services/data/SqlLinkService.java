package backend.academy.scrapper.services.data;

import backend.academy.scrapper.entities.Link;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@ConditionalOnProperty(name = "access-type", havingValue = "SQL")
public class SqlLinkService extends LinkService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SqlLinkService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Link> getAllLinks() {
        return jdbcTemplate.query(
            "SELECT url, tags, filters, update FROM link",
            new BeanPropertyRowMapper<>(Link.class)
        ).stream().collect(Collectors.toSet());
    }

    @Override
    public void createChatById(Long id) {
        jdbcTemplate.update(
            """
                INSERT INTO chat(id) values (?)
                ON CONFLICT (id) DO NOTHING
            """,
            id
        );
    }

    @Override
    public void deleteChatById(Long id) {
        jdbcTemplate.update(
            "DELETE FROM chat where id=?",
            id
        );
    }

    @Override
    public Set<Link> getLinksByChatId(Long chatId) {
        return jdbcTemplate.query(
            """
                SELECT url, tags, filters, update
                FROM link WHERE chat_id=?
            """,
            new Object[]{chatId},
            new BeanPropertyRowMapper<>(Link.class)
        ).stream().collect(Collectors.toSet());
    }

    @Override
    public void addLink(Long chatId, Link link) {
        log.info("Chat: {}", chatId);
        jdbcTemplate.update(
            """
            INSERT INTO link(url, tags, filters, update, chat_id)
            VALUES (?,?,?,null,?)
            """,
            link.url(), String.join(", ", link.tags()),
            String.join(", ", link.filters()), chatId
        );
    }

    /**
     * Удаление ссылки по url в определенном чате
     * @param chatId id чата
     * @param url этой ссылки
     * @return ссылка, которая была удалена
     */
    @Override
    public Link removeLinkByUrl(long chatId, String url) {
        Link link = jdbcTemplate.query(
            """
                SELECT url, tags, filters, update
                FROM link WHERE url=? and chat_id=?
            """,
            new Object[]{url, chatId},
            new BeanPropertyRowMapper<>(Link.class)
        ).stream().findAny().orElse(null);
        jdbcTemplate.update(
                """
                DELETE FROM link WHERE url=? and chat_id=?
                """,
            url, chatId
        );
        return link;
    }

    @Override
    public List<Long> getIdsByLink(Link link) {
        return jdbcTemplate.query(
            """
                SELECT DISTINCT chat_id FROM link
                WHERE url=?
                ORDER BY chat_id
                """,
            new Object[]{link.url()},
            new BeanPropertyRowMapper<>(Long.class)
        );
    }
}
