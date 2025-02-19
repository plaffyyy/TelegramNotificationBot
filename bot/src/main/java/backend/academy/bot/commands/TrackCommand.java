package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.SneakyThrows;
import java.net.HttpURLConnection;
import java.net.URI;


public final class TrackCommand extends Command {
    public TrackCommand(long chatId, TelegramBot bot) {
        super(chatId, bot);
    }

    @SneakyThrows
    @Override
    public void execute() {

        bot.execute(new SendMessage(chatId, FileWithTextResponses.helpWords));

        URI uri = new URI("http://localhost:8081/links");


        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.connect();





    }
}
