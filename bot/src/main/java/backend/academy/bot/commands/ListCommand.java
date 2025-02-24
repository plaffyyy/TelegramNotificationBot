package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import com.pengrad.telegrambot.TelegramBot;

public final class ListCommand extends Command {

    public ListCommand(long chatId, TelegramBot bot) {
        super(chatId, bot, "");
    }

    @Override
    public void execute() {

    }
}
