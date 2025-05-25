package backend.academy.scrapper.services.updateSend;

import backend.academy.scrapper.entities.Link;
import backend.academy.scrapper.services.updateSend.kafka.SendNotificationKafka;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
public class SendNotificationHandler implements SendNotification {

    private final SendNotificationHttp httpSender;
    private final SendNotificationKafka kafkaSender;

    @Value("${message-transport}")
    private String mainTransport;

    public SendNotificationHandler(SendNotificationHttp httpSender, SendNotificationKafka kafkaSender) {
        this.httpSender = httpSender;
        this.kafkaSender = kafkaSender;
    }

    @Override
    public void sendUpdateToBot(Link link, List<Long> ids, String description) {
        if ("Kafka".equals(mainTransport)) {
            try {
                kafkaSender.sendUpdateToBot(link, ids, description);
            } catch (Exception kafkaEx) {
                log.error("Primary Kafka transport failed: {}", kafkaEx.getMessage());
                try {
                    httpSender.sendUpdateToBot(link, ids, description);
                    log.info("Fallback to HTTP succeeded");
                } catch (Exception httpEx) {
                    log.error("HTTP fallback also failed: {}", httpEx.getMessage());
                    throw new RuntimeException("Both transports failed", httpEx);
                }
            }
        } else {
            try {
                httpSender.sendUpdateToBot(link, ids, description);
            } catch (Exception httpEx) {
                log.error("Primary HTTP transport failed: {}", httpEx.getMessage());
                try {
                    kafkaSender.sendUpdateToBot(link, ids, description);
                    log.info("Fallback to Kafka succeeded");
                } catch (Exception kafkaEx) {
                    log.error("Kafka fallback also failed: {}", kafkaEx.getMessage());
                    throw new RuntimeException("Both transports failed", kafkaEx);
                }
            }
        }
    }
}
