package backend.academy.scrapper.clients_tests;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import backend.academy.scrapper.clients.StackOverflowClient;
import backend.academy.scrapper.exceptions.IncorrectLinkException;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

@Slf4j
public class StackOverflowTests {

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(new WireMockConfiguration().dynamicPort())
            .build();

    @DisplayName("Проверка того, что получаю ошибку, если нет такого вопроса на StackOverflow")
    @Test
    public void testHttp404Error() {
        StackOverflowClient client = new StackOverflowClient(null, "");
        String url = wireMockServer.baseUrl() + "/notfound";

        wireMockServer.stubFor(
                get(urlEqualTo("/notfound")).willReturn(aResponse().withStatus(404)));

        RuntimeException exception = assertThrows(IncorrectLinkException.class, () -> client.getApi("notfound"));
        assertTrue(exception.getMessage().contains("Incorrect Stack Overflow link"));
    }
}
