package backend.academy.scrapper.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LinkService {

    private Map<Long, String> userLink;

    public LinkService() {
        userLink = new HashMap<>();
    }

    public void track(long chatId, String url) {
        userLink.put(chatId, url);

        log.debug(userLink.toString());


    }

}
