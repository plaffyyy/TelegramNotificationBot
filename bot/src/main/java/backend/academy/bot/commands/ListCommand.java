package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;
import java.util.Set;

@Slf4j
public final class ListCommand extends Command {

    public ListCommand(long chatId, TelegramBot bot) {
        super(chatId, bot, "");
    }

    @Override
    public void execute() {

        bot.execute(new SendMessage(chatId, FileWithTextResponses.listWords));

        RestClient restClient = RestClient.builder().build();


        log.warn("restClient starts");

        Set response = restClient.get()
            .uri("http://scrapper:8081/links/{chatId}", chatId)
            .retrieve()
            .body(Set.class);

        log.warn(response.toString());

        String allLinks = String.join(System.lineSeparator(), response);

        log.warn("All links:" + allLinks);
        bot.execute(new SendMessage(chatId, allLinks));



    }
}
