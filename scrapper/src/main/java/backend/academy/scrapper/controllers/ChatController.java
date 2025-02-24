package backend.academy.scrapper.controllers;

import backend.academy.scrapper.dao.LinkService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/tg-chat")
public final class ChatController {

    @Autowired
    private final LinkService linkService;

    @PostMapping("/{id}")
    public void register(@PathVariable long id) {
        linkService.createChatById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        linkService.deleteChatById(id);
    }


}
