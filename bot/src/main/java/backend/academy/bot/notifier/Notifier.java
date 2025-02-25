package backend.academy.bot.notifier;

import com.pengrad.telegrambot.TelegramBot;
import java.util.List;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public final class Notifier {

    @Autowired
    private final TelegramBot bot;

    public void notifyUsers(List<Long> ids, String message) {

        for (Long id : ids) {

            bot.execute(new SendMessage(id, message));

        }

    }

}
