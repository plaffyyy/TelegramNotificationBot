package backend.academy.scrapper.controllers;


import backend.academy.scrapper.dao.LinkService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/links")
public class TrackController {

    @Autowired
    private final LinkService linkService;

    @ResponseBody
    @GetMapping
    public String getName() {

        return "test line";

    }

    @PostMapping
    public void trackLink(@RequestBody Map<String, Object> request) {

        Long chatId = Long.valueOf(String.valueOf(request.get("chatId")));
        String link = String.valueOf(request.get("link"));
        linkService.track(chatId, link);

        log.info(linkService.getLinksByChatId(chatId).toString());
    }

}
