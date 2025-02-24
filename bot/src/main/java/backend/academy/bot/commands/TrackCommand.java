package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;


@Slf4j
public final class TrackCommand extends Command {
    public TrackCommand(long chatId, TelegramBot bot, String link) {
        super(chatId, bot, link);
    }

    @SneakyThrows
    @Override
    public void execute() {


        RestClient restClient = RestClient.builder().build();

        Map<String, Object> jsonRequest = Map.of(
            "chatId", chatId,
            "link", link
        );

        ResponseEntity<Void> response = restClient.post()
            .uri("http://scrapper:8081/links")
            .contentType(MediaType.APPLICATION_JSON)
            .body(jsonRequest)
            .retrieve()
            .toBodilessEntity();


        int responseCode = response.getStatusCode().value();

        if (responseCode == HttpURLConnection.HTTP_OK) {
//            log.info("link is successful get");
            bot.execute(new SendMessage(chatId, FileWithTextResponses.successfulTrack));
        } else {
            bot.execute(new SendMessage(chatId, FileWithTextResponses.errorTrack));
        }


    }
}
