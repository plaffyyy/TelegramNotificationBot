package backend.academy.bot.command_usage;

import com.pengrad.telegrambot.TelegramBot;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class Command {

    protected final long chatId;
    protected final TelegramBot bot;

    public abstract void execute();

}
