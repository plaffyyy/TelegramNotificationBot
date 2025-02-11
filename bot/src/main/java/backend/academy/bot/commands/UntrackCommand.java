package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import com.pengrad.telegrambot.TelegramBot;

public final class UntrackCommand extends Command {
    public UntrackCommand(long chatId, TelegramBot bot) {
        super(chatId, bot);
    }

    @Override
    public void execute() {

    }
}
