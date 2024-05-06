package jsonparser;

import java.util.HashMap;
import java.util.List;

public class Json {
    private List<HashMap<String, Json>> content;

    private Json(List<HashMap<String, Json>> content) {
        this.content = content;
    }

    public static Json from(List<HashMap<String, Json>> content) {
        return new Json(content);
    }
}
