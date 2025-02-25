package backend.academy.bot.controllers;

import backend.academy.bot.dto.UpdateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/updates")
public class UpdatesController {

    @PostMapping
    public ResponseEntity<UpdateResponse> postUpdate() {

    }

}
