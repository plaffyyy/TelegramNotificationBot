package backend.academy.scrapper.controllers;


import backend.academy.scrapper.dao.LinkService;
import io.swagger.v3.oas.annotations.headers.Header;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
    @GetMapping("/{chatId}")
    public Set<String> getLinks(@PathVariable long chatId) {

        return linkService.getLinksByChatId(chatId);

    }

    @PostMapping
    public void trackLink(@RequestBody Map<String, Object> request) {

        Long chatId = Long.valueOf(String.valueOf(request.get("chatId")));
        String link = String.valueOf(request.get("link"));
        linkService.track(chatId, link);

        log.info(linkService.getLinksByChatId(chatId).toString());
    }

}
