package backend.academy.bot.controllers;

import backend.academy.bot.notifier.NotificationHandler;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/updates")
public class UpdatesController {

    private final NotificationHandler notificationHandler;

    @PostMapping
    @RateLimiter(name = "apiRateLimiter")
    @TimeLimiter(name = "httpTimeout")
    @Retry(name = "httpRetry")
    @CircuitBreaker(name = "httpCB")
    public CompletableFuture<ResponseEntity<Void>> postUpdate(@RequestBody Map<String, Object> request) {
        return CompletableFuture.supplyAsync(() -> {
            List<Long> ids = (List<Long>) request.getOrDefault("tgChatIds", List.of());
            String url = (String) request.get("url");
            String description = (String) request.get("description");

            notificationHandler.handleNotification(url, description, ids);
            return ResponseEntity.ok(null);
        });
    }
}
