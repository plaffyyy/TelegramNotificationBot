package backend.academy.scrapper.db_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import backend.academy.scrapper.model.Link;
import backend.academy.scrapper.repositories.LinkRepository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LinkRepositoryTests {

    private static Stream<Arguments> provideLinks() {

        return Stream.of(
                Arguments.of(1L, "https://github.com/plaffyyy/SpringMVCLearn"),
                Arguments.of(2L, "https://github.com/plaffyyy/SpringMVCLearn"));
    }

    @DisplayName("Проверка на корректность добавления ссылок")
    @ParameterizedTest
    @MethodSource("provideLinks")
    public void addLinkAndCreateChatTest(long id, String path) {
        LinkRepository linkRepository = new LinkRepository();
        Map<Long, Set<Link>> expectedMap = new HashMap<>();

        linkRepository.createChatById(id);
        expectedMap.putIfAbsent(id, new HashSet<>(Arrays.asList(new Link(path, null, null))));
        linkRepository.addLink(id, new Link(path, null, null));

        assertEquals(expectedMap, linkRepository.userLink());
    }

    private static Stream<Arguments> provideLinksForDuplicate() {

        return Stream.of(
                Arguments.of(1L, "https://github.com/plaffyyy/SpringMVCLearn"),
                Arguments.of(1L, "https://github.com/plaffyyy/SpringMVCLearn"));
    }

    @DisplayName("Проверка, что если добавлено несколько одинаковых ссылок")
    @ParameterizedTest
    @MethodSource("provideLinksForDuplicate")
    public void duplicateLinksByIdTest(long id, String path) {
        LinkRepository linkRepository = new LinkRepository();
        Map<Long, Set<Link>> expectedMap = new HashMap<>();

        linkRepository.createChatById(id);
        expectedMap.put(id, new HashSet<>(Set.of(new Link(path, null, null))));
        linkRepository.addLink(id, new Link(path, null, null));

        assertEquals(expectedMap, linkRepository.userLink());
    }

    @DisplayName("Проверка на корректность добавления ссылок вместе со всеми аттрибутами")
    @Test
    public void saveLinkAndGetLinksWithAttributesTest() {
        long chatId = 1L;
        Link link = new Link(
                "https://github.com/plaffyyy/SpringMVCLearn",
                List.of("first-tag"),
                List.of("first-filter", "second-filter"));
        LinkRepository linkRepository = new LinkRepository();

        linkRepository.createChatById(chatId);
        linkRepository.addLink(chatId, link);
        Set<Link> links = linkRepository.getAllLinks();

        for (Link linkFromRepo : links) {
            assertEquals(link, linkFromRepo);
        }
    }

    @DisplayName("Проверка что ссылка корретно удаляется")
    @Test
    public void removeLinkByUrlAndIdTest() {
        LinkRepository linkRepository = new LinkRepository();
        linkRepository.createChatById(1L);
        Link linkForRemove = new Link("https://github.com/plaffyyy/SpringMVCLearn", null, null);
        linkRepository.addLink(1L, linkForRemove);
        linkRepository.addLink(1L, new Link("https://github.com/plaffyyy", null, null));
        long id = 1L;
        String url = "https://github.com/plaffyyy/SpringMVCLearn";

        Link link = linkRepository.removeLinkByUrl(id, url);

        assertEquals(linkForRemove, link);
    }

    @DisplayName("Проверка, что все ссылки уникальные")
    @Test
    public void getIdsByLinkTest() {

        LinkRepository linkRepository = new LinkRepository();
        List<Long> expectedIds = List.of(1L, 2L);
        linkRepository.createChatById(1L);
        linkRepository.createChatById(2L);
        linkRepository.addLink(1L, new Link("https://github.com/plaffyyy/SpringMVCLearn", null, null));
        linkRepository.addLink(2L, new Link("https://github.com/plaffyyy/SpringMVCLearn", null, null));
        linkRepository.addLink(2L, new Link("https://github.com/plaffyyy", null, null));

        List<Long> ids = linkRepository.getIdsByLink(
                new Link("https://github.com/plaffyyy/SpringMVCLearn", List.of("first-tag"), List.of("first-filter")));

        assertEquals(expectedIds, ids);
    }
}
