package backend.academy.bot.controllers;

import backend.academy.bot.notifier.Notifier;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String description = (String) request.get("description");

        StringBuilder message = new StringBuilder();
        message.append("📢 Уведомление!\nНовое обновление в ссылке: ").append(url).append("\n");
        message.append(description);
        notifier.notifyUsers(ids, message.toString());

        return ResponseEntity.ok(null);
    }
}
