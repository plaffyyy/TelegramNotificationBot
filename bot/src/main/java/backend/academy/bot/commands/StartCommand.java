package backend.academy.bot.commands;


import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class StartCommand extends Command {


    public StartCommand(long chatId, TelegramBot bot) {
        super(chatId, bot, "");
    }

    @Override
    public void execute() {

        bot.execute(new SendMessage(chatId, FileWithTextResponses.firstWords));
        bot.execute(new SendMessage(chatId, FileWithTextResponses.startInformation));


    }
}
