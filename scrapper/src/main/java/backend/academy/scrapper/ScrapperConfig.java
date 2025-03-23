package backend.academy.scrapper;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ScrapperConfig(@NotEmpty String githubToken, StackOverflowCredentials stackOverflow) {
    @Bean
    public String gitHubToken() {
        return githubToken;
    }

    public record StackOverflowCredentials(@NotEmpty String key, @NotEmpty String accessToken) {}
}
