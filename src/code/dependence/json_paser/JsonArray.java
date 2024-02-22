package code.dependence.json_paser;

import java.util.ArrayList;
import java.util.List;

public class JsonArray implements JJson{
    public ArrayList<JsonValue<?>> arrayList;

    public static JsonArray buildJsonArray(List<Token> list) {
        var res = new JsonArray();
        if (list.get(0).type == Token.TokenType.ArrayBegin) {
            list.remove(0);
        }

        if (list.get(0).type == Token.TokenType.ArrayEnd) {
            list.remove(0);
            return res;
        }

        while (true) {
            var value = JsonValue.buildJsonValue(list);
            res.arrayList.add(value);
            if (list.get(0).type == Token.TokenType.ArrayEnd) {
                list.remove(0);
                break;
            }

            if (list.get(0).type == Token.TokenType.Comma) {
                list.remove(0);
            }
        }
        return res;
    }

    public JsonArray() {
        arrayList = new ArrayList<>();
    }


    @Override
    public String toString() {
        var res = new StringBuilder();
        res.append("JsonArray{len:").append(this.arrayList.size()).append(';');
        this.arrayList.forEach(
                value ->res.append('[').append(value.toString()).append("],"));
        res.append('}');
        return res.toString();
    }

    public boolean isContain(JsonValue<?> value) {
        return this.arrayList.contains(value);
    }

    public JsonValue<?> get(int index) {
        return this.arrayList.get(index);
    }

    @Override
    public JJson getElement(String name) {
        return null;
    }
}
