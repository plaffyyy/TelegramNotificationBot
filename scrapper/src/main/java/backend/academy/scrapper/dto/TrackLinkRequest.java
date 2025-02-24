package backend.academy.scrapper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TrackLinkRequest {

    private long chatId;
    private String link;

}
