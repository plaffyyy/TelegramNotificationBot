package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import backend.academy.bot.dto.LinkResponse;
import backend.academy.bot.model.Link;
import backend.academy.bot.services.CommandRequestService;
import backend.academy.bot.services.RedisCacheService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.HttpURLConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Set;

@Slf4j
public final class ListCommand extends Command {

    private final RedisCacheService redisCacheService;

    public ListCommand(
        long chatId, 
        TelegramBot bot, 
        CommandRequestService commandRequestService, 
        String tag,
        RedisCacheService redisCacheService
    ) {
        super(commandRequestService, chatId, bot, tag);
        this.redisCacheService = redisCacheService;
    }

    @Override
    public void execute() {
        try {
            bot.execute(new SendMessage(chatId, FileWithTextResponses.listWords));
            //url is a tag
            Set<Link> cachedLinks = redisCacheService.getCachedList(chatId);
            if (cachedLinks != null) {
                log.info("Using cached list for chatId: {}", chatId);
                handleLinksResponse(new LinkResponse(cachedLinks, cachedLinks.size()));
                return;
            }

            ResponseEntity<LinkResponse> response = commandRequestService.listCommandResponse(chatId, url);
            // log.info("Response for list operation: {}", response);
            
            int responseCode = response.getStatusCode().value();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                LinkResponse linkResponse = response.getBody();
                if (linkResponse == null || linkResponse.links().isEmpty()) {
                    bot.execute(new SendMessage(chatId, "Нет отслеживаемых ссылок."));
                    return;
                }
                
                // Cache the successful response
                redisCacheService.cacheListResult(chatId, linkResponse.links());
                
                handleLinksResponse(linkResponse);
            } else {
                bot.execute(new SendMessage(chatId, FileWithTextResponses.errorList));
            }
        } catch (HttpClientErrorException e) {
            bot.execute(new SendMessage(chatId, FileWithTextResponses.errorList));
        }
    }

    private void handleLinksResponse(LinkResponse linkResponse) {
        linkResponse.links().forEach(link -> log.info(link.url()));
        bot.execute(new SendMessage(chatId, formatingLinks(linkResponse)));
    }

    public String formatingLinks(LinkResponse linkResponse) {
        StringBuilder allLinks = new StringBuilder("Отслеживаемые ссылки:\n");
        linkResponse.links().forEach(link -> allLinks.append(link.url()).append("\n"));
        return allLinks.toString();
    }
}
