package dependence.json.ast;

import java.util.HashMap;
import java.util.Map;

public class JsonObject {
    private Map<String, Value> map = new HashMap<>();

    public Map<String, Value> getMap() {
        return map;
    }

}


