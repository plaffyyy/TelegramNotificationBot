package backend.academy.bot.command_usage;

import com.pengrad.telegrambot.TelegramBot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
@Getter
public abstract class Command {

    @Value("${url.links}")
    protected String urlForApi;

    @Value(("${url.tg-chats}"))
    protected String urlForChatApi;

    protected final Long chatId;
    protected final TelegramBot bot;
    protected final String url;

    public abstract void execute();
}
