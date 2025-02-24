package backend.academy.bot.components;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.CommandHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                try {
                    CommandHandler commandHandler = new CommandHandler(bot, update);
                    Command command = commandHandler.getCommandFromUpdate();
                    if (command == null) {
                    } else {
                        command.execute();
                    }
                } catch (Exception e) {
                    log.error(e);
                }

            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }


}
