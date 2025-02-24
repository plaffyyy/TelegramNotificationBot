package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import com.pengrad.telegrambot.TelegramBot;
import org.springframework.web.client.RestClient;

public final class UntrackCommand extends Command {
    public UntrackCommand(long chatId, TelegramBot bot, String url) {
        super(chatId, bot, url);
    }

    @Override
    public void execute() {



    }

}
