package backend.academy.bot.command_usage;

import backend.academy.bot.commands.HelpCommand;
import backend.academy.bot.commands.ListCommand;
import backend.academy.bot.commands.StartCommand;
import backend.academy.bot.commands.TrackCommand;
import backend.academy.bot.commands.UntrackCommand;
import backend.academy.bot.exceptions.NotFoundCommandException;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandHandler {

    private TelegramBot bot;
    private long chatId;
    private String textMessage;

    public Command getCommandFromUpdate() {

        String[] messageLink = textMessage.split(" ");

        try {
            return switch (messageLink[0]) {

                case "/start" -> new StartCommand(chatId, bot);
                case "/help" -> new HelpCommand(chatId, bot);
                case "/track" -> new TrackCommand(chatId, bot, messageLink[1]);
                case "/untrack" -> new UntrackCommand(chatId, bot, messageLink[1]);
                case "/list" -> new ListCommand(chatId, bot);
                default -> throw new NotFoundCommandException("Bot hasn't this command");
            };
        } catch (NotFoundCommandException e) {
            bot.execute(new SendMessage(chatId, "Нет такой команды"));
        }
        return null;

    }


}
