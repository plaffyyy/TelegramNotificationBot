package backend.academy.scrapper.services.updateSend;

import backend.academy.scrapper.entities.Link;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface SendNotification {
    void sendUpdateToBot(Link link, List<Long> ids, String description);
}
