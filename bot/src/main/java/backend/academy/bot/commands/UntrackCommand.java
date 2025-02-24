package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.model.AllLinks;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import backend.academy.bot.dto.TrackLinkResponse;
import java.net.HttpURLConnection;
import java.util.Map;

public final class UntrackCommand extends Command {
    public UntrackCommand(long chatId, TelegramBot bot, String url) {
        super(chatId, bot, url);
    }

    @Override
    public void execute() {

        untrackLink();

    }
    private void untrackLink() {
        RestTemplate restTemplate = new RestTemplate();

        String url = AllLinks.scrapperLinks;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Tg-Chat-Id", String.valueOf(chatId));


        Map<String, String> requestBody = Map.of("url", this.url);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<TrackLinkResponse> response = restTemplate.exchange(
            url, HttpMethod.DELETE, requestEntity, TrackLinkResponse.class
        );

        if (response.getStatusCode().value() == HttpURLConnection.HTTP_OK) {
            bot.execute(new SendMessage(chatId, "Ссылка успешно удалена!"));
        } else {
            bot.execute(new SendMessage(chatId, "Ошибка при удалении ссылки."));
        }
    }
}
