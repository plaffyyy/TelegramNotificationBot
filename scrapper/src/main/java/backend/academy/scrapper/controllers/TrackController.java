package backend.academy.scrapper.controllers;


import backend.academy.scrapper.dao.LinkService;
import backend.academy.scrapper.dto.LinkResponse;
import backend.academy.scrapper.dto.TrackLinkResponse;
import backend.academy.scrapper.model.Link;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final LinkService linkService;

    @ResponseBody
    @GetMapping
    public ResponseEntity<LinkResponse> getLinks(@PathVariable long chatId) {

        Set<Link> links = linkService.getLinksByChatId(chatId);

        LinkResponse linkResponse = new LinkResponse(links, links.size());
        return ResponseEntity.ok(linkResponse);

    }

    @PostMapping
    public ResponseEntity<TrackLinkResponse> trackLink(@RequestHeader("Tg-Chat-Id") Long chatId, @RequestBody Map<String, Object> request) {

        String url = String.valueOf(request.get("url"));
        List<String> tags = (List<String>) request.getOrDefault("tags", List.of());
        List<String> filters = (List<String>) request.getOrDefault("filters", List.of());

        Link link = new Link(url, tags, filters);

        linkService.track(chatId, link);
        log.info(linkService.getLinksByChatId(chatId).toString());

        TrackLinkResponse trackLinkResponse = new TrackLinkResponse(
            chatId, url, tags, filters
        );
        return ResponseEntity.ok(trackLinkResponse);
    }

}
