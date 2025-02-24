package backend.academy.bot.commands;

import backend.academy.bot.command_usage.AvailableCommands;
import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

public final class HelpCommand extends Command {
    public HelpCommand(long chatId, TelegramBot bot) {
        super(chatId, bot, "");
    }

    @Override
    public void execute() {

        bot.execute(new SendMessage(chatId, FileWithTextResponses.helpWords));
        StringBuilder infoMessage = new StringBuilder();

        AvailableCommands.commands.forEach((name, info) -> {
            infoMessage.append(name + " --- " + info + "\n");
        });

        bot.execute(new SendMessage(chatId, infoMessage.toString()));

    }
}
