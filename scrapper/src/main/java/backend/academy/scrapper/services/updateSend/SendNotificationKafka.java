package backend.academy.scrapper.services.updateSend;

import backend.academy.scrapper.entities.Link;
import java.util.List;

public class SendNotificationKafka implements SendNotification{
    @Override
    public void sendUpdateToBot(Link link, List<Long> ids, String description) {

    }
}
