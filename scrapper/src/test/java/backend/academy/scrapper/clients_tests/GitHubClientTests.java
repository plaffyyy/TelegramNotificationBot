package backend.academy.scrapper.clients_tests;

import backend.academy.scrapper.clients.GitHubClient;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertNull;


public class GitHubClientTests {

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
        .options(new WireMockConfiguration().dynamicPort())
        .build();
    @Test
    public void testHttp401Error() {
        GitHubClient client = new GitHubClient("");
        String url = wireMockServer.baseUrl() + "/notfound";

        wireMockServer.stubFor(get(urlEqualTo("/notfound"))
            .willReturn(aResponse().withStatus(401)));

        assertNull(client.getApi(url));
    }

}
