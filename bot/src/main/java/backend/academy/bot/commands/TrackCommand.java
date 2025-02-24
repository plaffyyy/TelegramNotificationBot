package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import backend.academy.bot.components.NotifierBot;
import backend.academy.bot.dto.TrackLinkResponse;
import backend.academy.bot.model.AllLinks;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;


@Slf4j
public final class TrackCommand extends Command {
    private String[] tags;
    private String[] filters;

    private final CompletableFuture<Void> waitForTags = new CompletableFuture<>();
    private final CompletableFuture<Void> waitForFilters = new CompletableFuture<>();

    public TrackCommand(long chatId, TelegramBot bot, String url) {
        super(chatId, bot, url);
    }


    @SneakyThrows
    @Override
    public void execute() {
        bot.execute(new SendMessage(chatId, "Введите теги (опционально) через пробел"));
        NotifierBot.waitingForTags.put(chatId, this);

        waitForTags.thenRun(() -> {
            bot.execute(new SendMessage(chatId, "Настройте фильтры (опционально) через пробел"));
            NotifierBot.waitingForFilters.put(chatId, this);

            waitForFilters.thenRun(this::addLink);
        });


    }

    private void addLink() {
        RestClient restClient = RestClient.builder().build();

        Map<String, Object> jsonRequest = Map.of(
            "url", url,
            "tags", List.of(tags),
            "filters", List.of(filters)
        );

        ResponseEntity<TrackLinkResponse> response = restClient.post()
            .uri(AllLinks.scrapperLinks)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .body(jsonRequest)
            .retrieve()
            .toEntity(TrackLinkResponse.class);

        //check my tags and filters
        log.info("Tags: " +  tags);
        log.info("Filters: " + filters);

        int responseCode = response.getStatusCode().value();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            bot.execute(new SendMessage(chatId, FileWithTextResponses.successfulTrack));
        } else {
            bot.execute(new SendMessage(chatId, FileWithTextResponses.errorTrack));
        }
    }

    public void setTagsAndNotifyFuture(String tags) {
        this.tags = tags.split(" ");
        waitForTags.complete(null);
    }

    public void setFiltersAndNotifyFuture(String filters) {
        this.filters = filters.split(" ");
        waitForFilters.complete(null);
    }
}
