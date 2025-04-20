package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import backend.academy.bot.dto.TrackLinkResponse;
import backend.academy.bot.exceptions.IncorrectLinkForDelete;
import backend.academy.bot.services.CommandRequestService;
import backend.academy.bot.services.RedisCacheService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.HttpURLConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@Slf4j
public final class UntrackCommand extends Command {

    private final RedisCacheService redisCacheService;

    public UntrackCommand(
        long chatId, 
        TelegramBot bot, 
        CommandRequestService commandRequestService, 
        String url,
        RedisCacheService redisCacheService
    ) {
        super(commandRequestService, chatId, bot, url);
        this.redisCacheService = redisCacheService;
    }

    @Override
    public void execute() {
        try {
            if (url == null || url.isEmpty()) {
                throw new IncorrectLinkForDelete("Ошибка в ссылке");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Tg-Chat-Id", String.valueOf(chatId));

            Map<String, String> requestBody = Map.of("url", this.url);
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            bot.execute(new SendMessage(chatId, "Удаляю ссылку..."));
            ResponseEntity<TrackLinkResponse> response = commandRequestService.untrackCommandResponse(requestEntity);
            log.info("Response for untrack operation: {}", response);
            
            int responseCode = response.getStatusCode().value();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Invalidate the cache since we removed a link
                redisCacheService.changeListCache(chatId);
                bot.execute(new SendMessage(chatId, "Ссылка успешно удалена: " + this.url));
            } else {
                bot.execute(new SendMessage(chatId, "Ошибка при удалении ссылки"));
            }
        } catch (IncorrectLinkForDelete e) {
            bot.execute(new SendMessage(chatId, "Ошибка при удалении ссылки"));
        }
    }
}
