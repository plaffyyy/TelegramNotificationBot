package backend.academy.scrapper.controllers;

import backend.academy.scrapper.services.data.LinkService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/tg-chat")
public final class ChatController {

    private final LinkService linkService;

    @PostMapping("/{id}")
    @RateLimiter(name = "apiRateLimiter")
    @TimeLimiter(name = "httpTimeout")
    @Retry(name = "httpRetry")
    @CircuitBreaker(name = "httpCB")
    public Mono<Void> register(@PathVariable Long id) {
        return Mono.fromRunnable(() -> {
            linkService.createChatById(id);
        });
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "apiRateLimiter")
    @TimeLimiter(name = "httpTimeout")
    @Retry(name = "httpRetry")
    @CircuitBreaker(name = "httpCB")
    public Mono<Void> delete(@PathVariable Long id) {
        return Mono.fromRunnable(() -> {
            linkService.deleteChatById(id);
        });    }
}
