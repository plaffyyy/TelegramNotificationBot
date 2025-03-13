package backend.academy.bot.commands;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.FileWithTextResponses;
import backend.academy.bot.components.NotifierBot;
import backend.academy.bot.dto.TrackLinkResponse;
import backend.academy.bot.services.CommandRequestService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public final class TrackCommand extends Command {

    private List<String> tags;
    private List<String> filters;

    private final CompletableFuture<Void> waitForTags = new CompletableFuture<>();
    private final CompletableFuture<Void> waitForFilters = new CompletableFuture<>();

    public TrackCommand(long chatId, TelegramBot bot, CommandRequestService commandRequestService, String url) {
        super(commandRequestService, chatId, bot, url);
    }

    @SneakyThrows
    @Override
    public void execute() {
        bot.execute(new SendMessage(chatId, "Введите теги (опционально, введите - если не надо) через пробел"));
        NotifierBot.waitingForTags.put(chatId, this);

        waitForTags.thenRun(() -> {
            bot.execute(
                    new SendMessage(chatId, "Настройте фильтры (опционально, введите - если не надо) через пробел"));
            NotifierBot.waitingForFilters.put(chatId, this);

            waitForFilters.thenRun(this::addLink);
        });
    }

    private void addLink() {

        Map<String, Object> jsonRequest = Map.of(
                "url", url,
                "tags", tags,
                "filters", filters);

        ResponseEntity<TrackLinkResponse> response = commandRequestService.trackCommandResponse(jsonRequest, chatId);

        int responseCode = response.getStatusCode().value();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            bot.execute(new SendMessage(chatId, FileWithTextResponses.successfulTrack));
        } else {
            bot.execute(new SendMessage(chatId, FileWithTextResponses.errorTrack));
        }
    }

    public void setTagsAndNotifyFuture(String tags) {
        if (!tags.equals("-")) {
            this.tags = List.of(tags.split(" "));
        } else {
            this.tags = new ArrayList<>();
        }
        waitForTags.complete(null);
    }

    public void setFiltersAndNotifyFuture(String filters) {
        if (!filters.equals("-")) {
            this.filters = List.of(filters.split(" "));
        } else {
            this.filters = new ArrayList<>();
        }
        waitForFilters.complete(null);
    }
}
