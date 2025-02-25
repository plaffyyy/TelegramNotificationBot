package backend.academy.bot.controllers;

import backend.academy.bot.notifier.Notifier;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/updates")
public class UpdatesController {

    @Autowired
    private final Notifier notifier;

    @PostMapping
    public ResponseEntity<Void> postUpdate(@RequestBody Map<String, Object> request) {
        List<Long> ids = (List<Long>) request.getOrDefault("tgChatIds", List.of());
        String url = (String) request.get("url");

        log.warn("Ids in controller: " + ids.toString());
        String message = "📢 Уведомление!\nНовое обновление в ссылке : " + url;
        notifier.notifyUsers(ids, message);


        return ResponseEntity.ok(null);
    }

}
