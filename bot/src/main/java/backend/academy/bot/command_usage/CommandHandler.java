package backend.academy.bot.command_usage;

import backend.academy.bot.commands.HelpCommand;
import backend.academy.bot.commands.ListCommand;
import backend.academy.bot.commands.StartCommand;
import backend.academy.bot.commands.TrackCommand;
import backend.academy.bot.commands.UntrackCommand;
import backend.academy.bot.exceptions.NotFoundCommandException;
import backend.academy.bot.services.CommandRequestService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandHandler {

    private final TelegramBot bot;
    private final long chatId;
    private final String textMessage;
    private final CommandRequestService commandRequestService;

    public Command getCommandFromUpdate() {

        String[] messageLink = textMessage.split(" ");

        try {
            return switch (messageLink[0]) {
                case "/start" -> new StartCommand(chatId, bot, commandRequestService);
                case "/help" -> new HelpCommand(chatId, bot, commandRequestService);
                case "/track" -> {
                    String url = messageLink.length == 2 ? messageLink[1] : "";
                    if (url.isEmpty()) {
                        throw new NotFoundCommandException("Введите ссылку в команде");
                    }
                    yield new TrackCommand(chatId, bot, commandRequestService, url);
                }
                case "/untrack" -> {
                    String url = messageLink.length == 2 ? messageLink[1] : "";
                    if (url.isEmpty()) {
                        throw new NotFoundCommandException("Введите ссылку в команде");
                    }
                    yield new UntrackCommand(chatId, bot, commandRequestService, url);
                }
                case "/list" -> new ListCommand(chatId, bot, commandRequestService);
                default -> throw new NotFoundCommandException("У бота нет такой команды");
            };
        } catch (NotFoundCommandException e) {
            bot.execute(new SendMessage(chatId, "Нет такой команды"));
        }
        return null;
    }
}
