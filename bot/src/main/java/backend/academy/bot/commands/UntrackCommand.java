package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.dto.TrackLinkResponse;
import backend.academy.bot.exceptions.IncorrectLinkForDelete;
import backend.academy.bot.model.AllLinks;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.HttpURLConnection;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

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

        // TODO: do correct handle exceptions
        try {
            ResponseEntity<TrackLinkResponse> response =
                    restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, TrackLinkResponse.class);

            int responseCode = response.getStatusCode().value();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                bot.execute(new SendMessage(chatId, "Ссылка успешно удалена: " + this.url));
            } else {
                throw new IncorrectLinkForDelete("Ошибка в ссылке");
            }

        } catch (RestClientResponseException e) {
            String errorMessage = "Ошибка при удалении ссылки";
            bot.execute(new SendMessage(chatId, errorMessage));

        } catch (Exception e) {
            bot.execute(new SendMessage(chatId, "Произошла ошибка при удалении ссылки. Попробуйте позже."));
        }
    }
}
