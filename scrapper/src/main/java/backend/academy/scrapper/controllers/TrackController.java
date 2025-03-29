package backend.academy.scrapper.controllers;

import backend.academy.scrapper.dto.LinkResponse;
import backend.academy.scrapper.dto.TrackLinkResponse;
import backend.academy.scrapper.entities.Chat;
import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.exceptions.LinkNotFoundException;
import backend.academy.scrapper.services.data.LinkService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/links")
public final class TrackController {

    private final LinkService linkService;

    /**
     * Получение всех ссылок из базы данных
     * их преобразование в объекты Link из папки model
     * @return все ссылки
     */
    @ResponseBody
    @GetMapping
    public ResponseEntity<LinkResponse> getLinks(@RequestHeader("Tg-Chat-Id") String id) {
        Long chatId = Long.valueOf(id);
        Set<Link> links = linkService.getLinksByChatId(chatId);
        log.info("Links by id in controller {}: {}", chatId, links);
        Set<backend.academy.scrapper.model.Link> linksForResponse = links.stream()
            .map(link -> new backend.academy.scrapper.model.Link(link.id(), link.url(),
                link.tags(), link.filters(),
                link.update(), new backend.academy.scrapper.model.Chat(chatId)))
            .collect(Collectors.toSet());

        LinkResponse linkResponse = new LinkResponse(linksForResponse, links.size());
        log.info("LinkResponse by id in controller: {}", linkResponse);

        return ResponseEntity.ok(linkResponse);
    }

    @PostMapping
    public ResponseEntity<TrackLinkResponse> trackLink(
            @RequestHeader("Tg-Chat-Id") Long chatId, @RequestBody Map<String, Object> request) {
        log.info("Just log for check that controller get this");

        String url = String.valueOf(request.get("url"));

        List<String> tags = (List<String>) request.getOrDefault("tags", List.of());
        List<String> filters = (List<String>) request.getOrDefault("filters", List.of());

        Link link = new Link(url, tags, filters);

        log.info("Link for adding: {} and chatId: {}", link, chatId);

        linkService.addLink(chatId, link);
        log.info("links by id {}", linkService.getLinksByChatId(chatId).toString());

        TrackLinkResponse trackLinkResponse = new TrackLinkResponse(chatId, url, tags, filters);
        log.info("TrackLinkResponse url: {}", trackLinkResponse.url());
        return ResponseEntity.ok(trackLinkResponse);
    }

    @DeleteMapping
    public ResponseEntity<TrackLinkResponse> deleteLink(
            @RequestHeader("Tg-Chat-Id") String id, @RequestBody Map<String, Object> request) {
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
    }
}
