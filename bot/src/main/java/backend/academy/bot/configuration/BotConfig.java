package backend.academy.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record BotConfig(@Value("${app.telegram-token}") @NotEmpty String telegramToken) {

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(telegramToken);
    }
}
