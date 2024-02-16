package code.dependence.json_paser;


import java.util.List;


public class JsonValue<T> {
    public T t;
    public static JsonValue<?> buildJsonValue(List<Token> list) {
       var res = new JsonValue<>();
        if (list.get(0).type == Token.TokenType.ObjectBegin) {
            res.t = JsonObject.buildJsonObject(list);
        } else if (list.get(0).type == Token.TokenType.ArrayBegin) {
            res.t = JsonArray.buildJsonArray(list);
        } else if (list.get(0).type == Token.TokenType.String) {
            res.t = list.remove(0).value;
        } else if (list.get(0).type == Token.TokenType.Bool) {
            switch (list.remove(0).value) {
                case "true" -> res.t = Boolean.TRUE;
                case "false" -> res.t = Boolean.FALSE;
            }
        } else if (list.get(0).type == Token.TokenType.Number) {
            res.t = Double.valueOf(list.remove(0).value);
        } else if (list.get(0).type == Token.TokenType.Null) {
            res.t = "$NULL$";
            list.remove(0);
        }
        return res;
    }

    public Double toDouble() {
        return (Double) t;
    }
    public Integer toInteger() {
        return (Integer) t;
    }

    public Boolean toBoolean() {
        return (Boolean) t;
    }

    public JsonObject toJsonObject() {
        return (JsonObject) t;
    }

    @Override
    public String toString() {
        return t.toString();
    }
}
