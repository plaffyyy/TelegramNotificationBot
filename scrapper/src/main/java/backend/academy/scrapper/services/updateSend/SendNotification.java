package backend.academy.scrapper.services.updateSend;

import backend.academy.scrapper.entities.Link;
import java.util.List;

public interface SendNotification {
    void sendUpdateToBot(Link link, List<Long> ids);
}
