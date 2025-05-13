package backend.academy.scrapper.controllers;

import backend.academy.scrapper.dto.LinkResponse;
import backend.academy.scrapper.dto.TrackLinkResponse;
import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.exceptions.LinkNotFoundException;
import backend.academy.scrapper.model.ChatDto;
import backend.academy.scrapper.model.LinkDto;
import backend.academy.scrapper.services.data.LinkService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/links")
public final class TrackController {

    private final LinkService linkService;

    /**
     * Получение всех ссылок из базы данных их преобразование в объекты Link из папки model
     * Использование аннотаций - это паттерн декоратор, работает по следующему принципу:
     * Mono ← CircuitBreaker ← Retry ← TimeLimiter ← RateLimiter ← Mono.fromCallable()
     *
     *
     * @return все ссылки
     */
    @ResponseBody
    @GetMapping
    @RateLimiter(name = "apiRateLimiter")
    @TimeLimiter(name = "httpTimeout")
    @Retry(name = "httpRetry")
    @CircuitBreaker(name = "httpCB")
    public Mono<ResponseEntity<LinkResponse>> getLinks(
            @RequestHeader("Tg-Chat-Id") String id, @RequestHeader("tag") String tag) {

        return Mono.fromCallable(() -> {
            long chatId = Long.parseLong(id);
            Set<Link> links;
            if (tag.isEmpty()) {
                links = linkService.getLinksByChatId(chatId);
            } else {
                links = linkService.getLinksByChatIdAndTag(chatId, tag);
            }
            log.info("Links by id in controller {}: {}", chatId, links);
            Set<LinkDto> linksForResponse = links.stream()
                .map(link -> new LinkDto(
                    link.id(), link.url(), link.tags(), link.filters(), link.update(), new ChatDto(chatId)))
                .collect(Collectors.toSet());
            log.info("LinksForResponse by id in controller {}: {}", chatId, linksForResponse);
            LinkResponse linkResponse = new LinkResponse(linksForResponse, links.size());
            log.info("LinkResponse by id in controller: {}", linkResponse);

            return ResponseEntity.ok(linkResponse);
        });

    }

    @PostMapping
    @RateLimiter(name = "apiRateLimiter")
    @TimeLimiter(name = "httpTimeout")
    @Retry(name = "httpRetry")
    @CircuitBreaker(name = "httpCB")
    public Mono<ResponseEntity<TrackLinkResponse>> trackLink(
            @RequestHeader("Tg-Chat-Id") String chatId, @RequestBody Map<String, Object> request) {

        return Mono.fromCallable(() -> {
            log.info("Just log for check that controller get this");

            String url = String.valueOf(request.get("url"));
            long chatID = Long.parseLong(chatId);

            List<String> tags = (List<String>) request.getOrDefault("tags", List.of());
            List<String> filters = (List<String>) request.getOrDefault("filters", List.of());

            Link link = new Link(url, tags, filters);

            linkService.addLink(chatID, link);
            log.info("links by id {}", linkService.getLinksByChatId(chatID).toString());

            TrackLinkResponse trackLinkResponse = new TrackLinkResponse(chatID, url, tags, filters);
            log.info("TrackLinkResponse url: {}", trackLinkResponse.url());
            return ResponseEntity.ok(trackLinkResponse);
        });

    }

    @DeleteMapping
    @RateLimiter(name = "apiRateLimiter")
    @TimeLimiter(name = "httpTimeout")
    @Retry(name = "httpRetry")
    @CircuitBreaker(name = "httpCB")
    public Mono<ResponseEntity<TrackLinkResponse>> deleteLink(
            @RequestHeader("Tg-Chat-Id") String id, @RequestBody Map<String, Object> request) {
        return Mono.fromCallable(() -> {
            long chatId = Long.parseLong(id);
            String url = String.valueOf(request.get("url"));
            try {
                Link link = linkService.removeLinkByUrl(chatId, url);
                if (link == null) {
                    throw new LinkNotFoundException("Ссылка " + url + " не найдена");
                }

                TrackLinkResponse trackLinkResponse =
                    new TrackLinkResponse(chatId, link.url(), link.tags(), link.filters());

                return ResponseEntity.ok(trackLinkResponse);
            } catch (Exception e) {
                return ResponseEntity.status(500).body(null);
            }
        });
    }
}
