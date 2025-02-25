package backend.academy.scrapper.repositories;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UpdateRepository {

    private Map<String, JSONArray> urlUpdate;

    public UpdateRepository() {
        urlUpdate = new HashMap<>();
    }


}
