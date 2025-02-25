package backend.academy.bot.command_usage;

import com.pengrad.telegrambot.TelegramBot;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class Command {

    protected final Long chatId;
    protected final TelegramBot bot;
    protected final String url;

    public abstract void execute();

}
