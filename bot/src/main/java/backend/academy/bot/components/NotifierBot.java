package backend.academy.bot.components;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.CommandHandler;
import backend.academy.bot.commands.TrackCommand;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@AllArgsConstructor
@Component
public final class NotifierBot {

    @Autowired
    private final TelegramBot bot;

    public static final Map<Long, TrackCommand> waitingForTags = new ConcurrentHashMap<>();
    public static final Map<Long, TrackCommand> waitingForFilters = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {

                if (update.message() == null) {
                    continue;
                }
                long chatId = update.message().chat().id();
                String text = update.message().text();


                if (waitingForTags.containsKey(chatId)) {
                    TrackCommand trackCommand = waitingForTags.remove(chatId);
                    trackCommand.setTagsAndNotifyFuture(text);
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                }


                if (waitingForFilters.containsKey(chatId)) {
                    TrackCommand trackCommand = waitingForFilters.remove(chatId);
                    trackCommand.setFiltersAndNotifyFuture(text);
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                }


                CommandHandler commandHandler = new CommandHandler(bot, chatId, text);
                Command command = commandHandler.getCommandFromUpdate();
                if (command != null) {
                    command.execute();
                }
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }


}
