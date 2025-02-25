package backend.academy.scrapper.controllers;

import backend.academy.scrapper.repositories.LinkRepository;
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
    private final LinkRepository linkRepository;

    @PostMapping("/{id}")
    public void register(@PathVariable Long id) {
        linkRepository.createChatById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        linkRepository.deleteChatById(id);
    }


}
