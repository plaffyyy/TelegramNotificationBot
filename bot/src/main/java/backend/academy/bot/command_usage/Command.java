package backend.academy.bot.command_usage;

import backend.academy.bot.services.CommandRequestService;
import com.pengrad.telegrambot.TelegramBot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Getter
@Component
public abstract class Command {
    public Command(Long chatId, TelegramBot bot, String url) {
        this.chatId = chatId;
        this.bot = bot;
        this.url = url;
    }

    @Autowired
    protected CommandRequestService commandRequestService;

    protected Long chatId;
    protected TelegramBot bot;
    protected String url;

    public abstract void execute();
}
