package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import backend.academy.bot.services.CommandRequestService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.HttpURLConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public final class StartCommand extends Command {

    public StartCommand(long chatId, TelegramBot bot, CommandRequestService commandRequestService) {
        super(commandRequestService, chatId, bot, "");
    }

    @Override
    public void execute() {
        try {
            bot.execute(new SendMessage(chatId, FileWithTextResponses.firstWords));
            bot.execute(new SendMessage(chatId, FileWithTextResponses.startInformation));

            ResponseEntity<Void> response = commandRequestService.startCommandResponse(chatId);

            int responseCode = response.getStatusCode().value();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                bot.execute(new SendMessage(chatId, FileWithTextResponses.successfulStart));
            } else {
                bot.execute(new SendMessage(chatId, FileWithTextResponses.errorStart));
            }
        } catch (Exception e) {
            return;
        }
    }
}
