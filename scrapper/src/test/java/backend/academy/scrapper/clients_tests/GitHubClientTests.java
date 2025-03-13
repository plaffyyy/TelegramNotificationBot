package backend.academy.scrapper.clients_tests;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertNull;

import backend.academy.scrapper.clients.GitHubClient;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class GitHubClientTests {

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(new WireMockConfiguration().dynamicPort())
            .build();

    @DisplayName("Проверка того, что ничего не получаю с некорректной ссылки, так как при ошибке выдается Null")
    @Test
    public void testHttp401Error() {
        GitHubClient client = new GitHubClient("");
        String url = wireMockServer.baseUrl() + "/notfound";

        wireMockServer.stubFor(
                get(urlEqualTo("/notfound")).willReturn(aResponse().withStatus(401)));

        assertNull(client.getApi(url));
    }
}
