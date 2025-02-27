package backend.academy.scrapper.notifier_tests;

import backend.academy.scrapper.clients.Client;
import backend.academy.scrapper.clients.ClientHandler;
import backend.academy.scrapper.clients.GitHubClient;
import backend.academy.scrapper.model.Link;
import backend.academy.scrapper.repositories.LinkRepository;
import backend.academy.scrapper.repositories.UpdateRepository;
import backend.academy.scrapper.updates.LinkUpdateChecker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UpdateCheckerTests {

    ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Test
    public void checkForUpdatesTest() {

        LinkRepository linkRepository = new LinkRepository();
        UpdateRepository updateRepository = new UpdateRepository();
        linkRepository.createChatById(1L);
        Link link = new Link("https://github.com/plaffyyy/SpringMVCLearn", null, null);
        linkRepository.addLink(1L, link);

        ClientHandler clientHandlerMock = Mockito.mock(ClientHandler.class);
        Client clientMock = Mockito.mock(GitHubClient.class);
        Mockito.when(clientHandlerMock.handleClients(Mockito.anyString())).thenReturn(clientMock);

        JsonNode mockResponse = objectMapper.readTree("{\"update\": \"new_commit\"}");
        Mockito.when(clientMock.getApi(Mockito.anyString())).thenReturn(mockResponse);

        LinkUpdateChecker linkUpdateChecker = new LinkUpdateChecker(linkRepository, updateRepository, clientHandlerMock);
        linkUpdateChecker.checkForUpdates();

        JsonNode lastUpdate = updateRepository.getLastUpdate(link.url());
        assertNotNull(lastUpdate);

    }


}
