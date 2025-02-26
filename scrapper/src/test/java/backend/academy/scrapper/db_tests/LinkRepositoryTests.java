package backend.academy.scrapper.db_tests;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinkRepositoryTests {

    private static Stream<Arguments> provideLinks() {

        return Stream.of(
            Arguments.of(1L, "https://github.com/plaffyyy/SpringMVCLearn"),
            Arguments.of(2L, "https://github.com/plaffyyy/SpringMVCLearn")
        );
    }

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
            Arguments.of(1L, "https://github.com/plaffyyy/SpringMVCLearn")
        );
    }
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

    @Test
    public void saveLinkAndGetLinksWithAttributesTest() {
        long chatId = 1L;
        Link link = new Link("https://github.com/plaffyyy/SpringMVCLearn", List.of("first-tag"), List.of("first-filter", "second-filter"));
        LinkRepository linkRepository = new LinkRepository();


        linkRepository.createChatById(chatId);
        linkRepository.addLink(chatId, link);
        Set<Link> links = linkRepository.getAllLinks();

        for (Link linkFromRepo: links) {
            assertEquals(link, linkFromRepo);
        }


    }
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
}
