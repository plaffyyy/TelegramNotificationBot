package backend.academy.scrapper.controllers;


import backend.academy.scrapper.repositories.LinkRepository;
import backend.academy.scrapper.dto.LinkResponse;
import backend.academy.scrapper.dto.TrackLinkResponse;
import backend.academy.scrapper.exceptions.LinkNotFoundException;
import backend.academy.scrapper.model.Link;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/links")

public final class TrackController {

    @Autowired
    private final LinkRepository linkRepository;

    @ResponseBody
    @GetMapping
    public ResponseEntity<LinkResponse> getLinks(@RequestHeader("Tg-Chat-Id") String id) {
        Long chatId = Long.valueOf(id);
        Set<Link> links = linkRepository.getLinksByChatId(chatId);

        LinkResponse linkResponse = new LinkResponse(links, links.size());


        return ResponseEntity.ok(linkResponse);

    }

    @PostMapping
    public ResponseEntity<TrackLinkResponse> trackLink(@RequestHeader("Tg-Chat-Id") String id, @RequestBody Map<String, Object> request) {
        Long chatId = Long.valueOf(id);
        String url = String.valueOf(request.get("url"));

        List<String> tags = (List<String>) request.getOrDefault("tags", List.of());
        List<String> filters = (List<String>) request.getOrDefault("filters", List.of());

        Link link = new Link(url, tags, filters);

        linkRepository.addLink(chatId, link);
        log.info(linkRepository.getLinksByChatId(chatId).toString());

        TrackLinkResponse trackLinkResponse = new TrackLinkResponse(
            chatId, url, tags, filters
        );
        return ResponseEntity.ok(trackLinkResponse);
    }

    @DeleteMapping
    public ResponseEntity<TrackLinkResponse> deleteLink(@RequestHeader("Tg-Chat-Id") String id, @RequestBody Map<String, Object> request) {
        long chatId = Long.parseLong(id);
        String url = String.valueOf(request.get("url"));
        Link link = linkRepository.removeLinkByUrl(chatId, url);
        if (link == null) {
            throw new LinkNotFoundException("Ссылка " + url + " не найдена");
        }

        TrackLinkResponse trackLinkResponse = new TrackLinkResponse(
            chatId, link.url(), link.tags(), link.filters()
        );

        return ResponseEntity.ok(trackLinkResponse);
    }
}
