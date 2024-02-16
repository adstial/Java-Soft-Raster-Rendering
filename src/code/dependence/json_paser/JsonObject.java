package code.dependence.json_paser;

import java.util.HashMap;
import java.util.List;

public class JsonObject{
    public HashMap<String, JsonValue<?>> nameValueMap;
    public static JsonObject buildJsonObject(List<Token> list) {
        var res = new JsonObject();
        if (list.get(0).type == Token.TokenType.ObjectBegin) {
            list.remove(0);
        }

        if (list.get(0).type == Token.TokenType.ObjectEnd) {
            list.remove(0);
            return res;
        }

        while (true) {
            var name = list.remove(0).value;

            if (list.get(0).type == Token.TokenType.Colon) {
                list.remove(0);
            }

            var value = JsonValue.buildJsonValue(list);
            res.nameValueMap.put(name, value);

            if (list.get(0).type == Token.TokenType.ObjectEnd) {
                list.remove(0);
                break;
            }

            if (list.get(0).type == Token.TokenType.Comma) {
                list.remove(0);
            }
        }
        return res;
    }

    public JsonValue getJsonValue(String name) {
        return nameValueMap.get(name);
    }

    public JsonObject() {
        nameValueMap = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("JsonObject{number:").append(this.nameValueMap.size()).append(':');
        nameValueMap.forEach(
                (key,value) -> res.append("key:").append(key).append(",value:").append(value.toString()).append(';')
        );
        res.append('}');
        return res.toString();
    }

}
