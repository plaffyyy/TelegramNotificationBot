package backend.academy.bot.notifier;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public final class Notifier {

    @Autowired
    private final TelegramBot bot;

    public void notifyUsers(List<Long> ids, String message) {

        for (int i = 0; i < ids.size(); i++) {
            bot.execute(new SendMessage(ids.get(i), message));
        }
    }
}
