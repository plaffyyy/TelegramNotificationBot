package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.dto.TrackLinkResponse;
import backend.academy.bot.exceptions.IncorrectLinkForDelete;
import backend.academy.bot.services.CommandRequestService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.HttpURLConnection;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;

public final class UntrackCommand extends Command {
    public UntrackCommand(long chatId, TelegramBot bot, CommandRequestService commandRequestService, String url) {
        super(commandRequestService, chatId, bot, url);
    }

    @Override
    public void execute() {
        untrackLink();
    }

    private void untrackLink() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Tg-Chat-Id", String.valueOf(chatId));

        Map<String, String> requestBody = Map.of("url", this.url);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<TrackLinkResponse> response = commandRequestService.untrackCommandResponse(requestEntity);

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
