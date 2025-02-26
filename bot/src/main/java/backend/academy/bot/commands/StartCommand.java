package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.HttpURLConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@Slf4j
public final class StartCommand extends Command {

    public StartCommand(long chatId, TelegramBot bot) {
        super(chatId, bot, "");
    }

    @Override
    public void execute() {

        bot.execute(new SendMessage(chatId, FileWithTextResponses.firstWords));
        bot.execute(new SendMessage(chatId, FileWithTextResponses.startInformation));

        RestClient restClient = RestClient.builder().build();

        ResponseEntity<Void> response = restClient
                .post()
                .uri("http://scrapper:8081/tg-chat/{chatId}", chatId)
                .retrieve()
                .toBodilessEntity();

        int responseCode = response.getStatusCode().value();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            bot.execute(new SendMessage(chatId, FileWithTextResponses.successfulStart));
        } else {
            bot.execute(new SendMessage(chatId, FileWithTextResponses.errorStart));
        }
    }
}
