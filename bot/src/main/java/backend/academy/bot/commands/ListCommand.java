package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import backend.academy.bot.dto.LinkResponse;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.HttpURLConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
public final class ListCommand extends Command {

    public ListCommand(long chatId, TelegramBot bot) {
        super(chatId, bot, "");
    }

    @Override
    public void execute() {
        try {
            bot.execute(new SendMessage(chatId, FileWithTextResponses.listWords));

            RestClient restClient = RestClient.builder().build();

            ResponseEntity<LinkResponse> response = restClient
                    .get()
                    .uri(urlForApi)
                    .header("Tg-Chat-Id", String.valueOf(chatId))
                    .retrieve()
                    .toEntity(LinkResponse.class);

            int responseCode = response.getStatusCode().value();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                LinkResponse linkResponse = response.getBody();
                if (linkResponse == null || linkResponse.links().isEmpty()) {
                    bot.execute(new SendMessage(chatId, "Нет отслеживаемых ссылок."));
                    return;
                }
                bot.execute(new SendMessage(chatId, formatingLinks(linkResponse)));
            } else {
                bot.execute(new SendMessage(chatId, FileWithTextResponses.errorList));
            }
        } catch (HttpClientErrorException e) {
            bot.execute(new SendMessage(chatId, FileWithTextResponses.errorList));
        }
    }

    public String formatingLinks(LinkResponse linkResponse) {
        StringBuilder allLinks = new StringBuilder("Отслеживаемые ссылки:\n");
        linkResponse.links().forEach(link -> allLinks.append(link.url()).append("\n"));
        return allLinks.toString();
    }
}
