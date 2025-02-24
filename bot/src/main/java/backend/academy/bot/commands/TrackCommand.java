package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public final class TrackCommand extends Command {
    public TrackCommand(long chatId, TelegramBot bot, String link) {
        super(chatId, bot, link);
    }

    @SneakyThrows
    @Override
    public void execute() {

        log.debug(link);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(Map.of(
            "chatId", chatId,
            "link", link
        ));

        URI uri = new URI("http://scrapper:8081/links");
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);


        OutputStream os = connection.getOutputStream();
        byte[] input = jsonInputString.getBytes("utf-8");
        os.write(input, 0, input.length);


        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            log.info("link is successful get");
            bot.execute(new SendMessage(chatId, "Ссылка успешно добавлена!"));
        } else {
            bot.execute(new SendMessage(chatId, "Ошибка при добавлении ссылки."));
        }


    }
}
