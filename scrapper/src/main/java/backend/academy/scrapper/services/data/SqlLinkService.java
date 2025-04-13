package backend.academy.scrapper.services.data;

import backend.academy.scrapper.entities.Chat;
import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.utils.converters.JsonConverter;
import backend.academy.scrapper.utils.converters.StringListConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "access-type", havingValue = "SQL")
public class SqlLinkService extends LinkService {

    private final JdbcTemplate jdbcTemplate;
    private final StringListConverter stringListConverter = new StringListConverter();
    private final JsonConverter jsonConverter = new JsonConverter();

    @Autowired
    public SqlLinkService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получение всех ссылок из базы данных их преобразование в объекты Link
     *
     * @return все ссылки
     */
    @Override
    public Set<Link> getAllLinks() {
        final int batchSize = 1000;
        List<Link> allLinks =
                jdbcTemplate.query("SELECT link.id, url, tags, filters, update, chat_id FROM link", getLinkRowMapper());

        return splitIntoBatches(allLinks, batchSize).stream()
                .flatMap(batch -> batch.stream().peek(link -> {
                    log.info("Processing link: {}", link.url());
                }))
                .collect(Collectors.toSet());
    }
    // вынос логики разбивки на батчи в отдельный метод. сделал дженериками, чтобы потом была возможность поменять
    // объект
    private <T> List<List<T>> splitIntoBatches(List<T> list, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            batches.add(list.subList(i, Math.min(i + batchSize, list.size())));
        }
        return batches;
    }

    @Override
    public void createChatById(Long id) {
        jdbcTemplate.update(
                """
                INSERT INTO chat(id) values (?)
                ON CONFLICT (id) DO NOTHING
            """,
                id);
    }

    @Override
    public void deleteChatById(Long id) {
        jdbcTemplate.update("DELETE FROM chat where id=?", id);
    }

    @Override
    public Set<Link> getLinksByChatId(Long chatId) {
        return jdbcTemplate
                .query(
                        """
                SELECT link.id, url, tags, filters, update, chat_id
                FROM link WHERE chat_id=?
            """,
                        new Object[] {chatId},
                        getLinkRowMapper())
                .stream()
                .collect(Collectors.toSet());
    }

    /**
     * Получение всех ссылок из базы данных по определенному id чата и тегу проверка по тегу идет, используя
     * преобразование в json
     *
     * @param chatId id чата
     * @param tag тег, по которому идет фильтрация
     * @return все ссылки по id чата и тегу
     */
    @Override
    public Set<Link> getLinksByChatIdAndTag(Long chatId, String tag) {
        String tagAsJson = "[\"" + tag + "\"]";
        return jdbcTemplate
                .query(
                        """
            SELECT link.id, url, tags, filters, update, chat_id
            FROM link
            WHERE chat_id = ? AND tags::jsonb @> ?::jsonb
            """,
                        new Object[] {chatId, tagAsJson},
                        getLinkRowMapper())
                .stream()
                .collect(Collectors.toSet());
    }

    @Override
    public void addLink(Long chatId, Link link) {
        log.info("Chat: {}", chatId);
        jdbcTemplate.update(
                """
            INSERT INTO link(url, tags, filters, update, chat_id)
            VALUES (?,?,?,null,?)
            """,
                link.url(),
                stringListConverter.convertToDatabaseColumn(link.tags()),
                stringListConverter.convertToDatabaseColumn(link.filters()),
                chatId);
    }
    // вынос логики поиска ссылки по url и chatId в отдельный метод
    private Link findLinkByUrl(long chatId, String url) {
        return jdbcTemplate
                .query(
                        """
                SELECT link.id, url, tags, filters, update, chat_id
                FROM link WHERE url=? and chat_id=?
            """,
                        new Object[] {url, chatId},
                        getLinkRowMapper())
                .stream()
                .findAny()
                .orElse(null);
    }

    /**
     * Удаление ссылки по url в определенном чате
     *
     * @param chatId id чата
     * @param url этой ссылки
     * @return ссылка, которая была удалена
     */
    @Override
    public Link removeLinkByUrl(long chatId, String url) {
        Link link = findLinkByUrl(chatId, url);
        jdbcTemplate.update(
                """
                DELETE FROM link WHERE url=? and chat_id=?
                """, url, chatId);
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
                new Object[] {link.url()},
                (rs, rowNum) -> rs.getLong("chat_id"));
    }

    @Override
    public JsonNode getUpdate(String url) {
        return jdbcTemplate
                .query(
                        """
                SELECT "update" FROM link WHERE url=?
                """,
                        new Object[] {url},
                        (rs, rowNum) -> {
                            String json = rs.getString("update");
                            return json != null ? jsonConverter.convertToEntityAttribute(json) : new ObjectNode(null);
                        })
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void changeUpdate(String url, JsonNode update) {
        jdbcTemplate.update(
                """
                UPDATE link SET update=?
                WHERE url=?
            """,
                jsonConverter.convertToDatabaseColumn(update),
                url);
    }

    /**
     * Преобразование в объекты Link из SQL кода
     *
     * @return RowMapper<Link> который содержит маппер этой ссылки
     */
    private @NotNull RowMapper<Link> getLinkRowMapper() {
        return (rs, rowNum) -> {
            Link l = new Link();
            l.url(rs.getString("url"));
            l.tags(stringListConverter.convertToEntityAttribute(rs.getString("tags")));
            l.filters(stringListConverter.convertToEntityAttribute(rs.getString("filters")));
            l.update(jsonConverter.convertToEntityAttribute(rs.getString("update")));
            Long chatId = rs.getLong("chat_id");
            l.chat(new Chat(chatId));
            return l;
        };
    }
}
