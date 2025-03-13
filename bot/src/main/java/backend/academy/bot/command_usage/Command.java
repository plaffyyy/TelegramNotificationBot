package backend.academy.bot.command_usage;

import backend.academy.bot.services.CommandRequestService;
import com.pengrad.telegrambot.TelegramBot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
public abstract class Command {

    protected final CommandRequestService commandRequestService;
    protected final Long chatId;
    protected final TelegramBot bot;
    protected final String url;

    public abstract void execute();
}
