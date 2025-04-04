package backend.academy.bot.components;

import static backend.academy.bot.command_usage.AvailableCommands.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.CommandHandler;
import backend.academy.bot.commands.TrackCommand;
import backend.academy.bot.services.CommandRequestService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@AllArgsConstructor
@Component
public final class NotifierBot {

    @Autowired
    private final TelegramBot bot;

    private final CommandRequestService commandRequestService;

    public static final Map<Long, TrackCommand> waitingForTags = new ConcurrentHashMap<>();
    public static final Map<Long, TrackCommand> waitingForFilters = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {

        registerCommands();

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {

                if (update.message() == null) {
                    log.info("Пришло пустое сообщение");
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

                CommandHandler commandHandler = new CommandHandler(bot, chatId, text, commandRequestService);
                Command command = commandHandler.getCommandFromUpdate();
                if (command != null) {
                    command.execute();
                }
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void registerCommands() {
        List<BotCommand> commandsList = new ArrayList<>();
        commands.forEach((c, d) -> {
            commandsList.add(new BotCommand(c, d));
        });
        log.info("My commands: {}", Arrays.toString(commandsList.toArray(new BotCommand[5])));
        bot.execute(new SetMyCommands(commandsList.toArray(new BotCommand[5])));
    }
}
