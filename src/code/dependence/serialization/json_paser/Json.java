package code.dependence.serialization.json_paser;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Json {
    public Object root;

    public String getString() {
        if (root instanceof String)
            return (String) root;
        else throw new RuntimeException("error");
    }

    public Double getDouble() {
        if (root.getClass() == Double.class)
            return (Double) root;
        else throw new RuntimeException("error");
    }

    public ArrayList<JsonValue<?>> getList() {
        if (root.getClass() == JsonArray.class)
            return ((JsonArray) root).arrayList;
        else {
            throw new RuntimeException("error");
        }
    }

    public static Json buildFromFile(String fileName) {
        Json res = new Json();
        StringBuilder context = new StringBuilder();
        String line;
        try (var br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                context.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        var tokenList = toTokenList(context.toString().replaceAll(" ",""));
        if (Objects.equals(tokenList.get(0).type, Token.TokenType.ObjectBegin)) {
            res.root = JsonObject.buildJsonObject(tokenList);
        } else if (Objects.equals(tokenList.get(0).type, Token.TokenType.ArrayBegin)) {
            res.root = JsonArray.buildJsonArray(tokenList);
        } else throw new RuntimeException("error");

        return res;
    }

    public static List<Token> toTokenList(String context) {
        var res = new ArrayList<Token>();
        var chars = context.toCharArray();

        boolean isNumber = false;
        StringBuilder number = new StringBuilder();

        boolean isString = false;
        StringBuilder string = new StringBuilder();

        boolean isBool = false;
        StringBuilder bool = new StringBuilder();
        for (var ch: chars) {
            if (ch == '"') {
                isString = !isString;
                if (!isString) {
                    res.add(new Token(Token.TokenType.String, string.toString()));
                    string = new StringBuilder();
                }
            }
            else if (ch == '{') {
                if (!isString) {
                    res.add(new Token(Token.TokenType.ObjectBegin, "{"));
                } else {
                    string.append(ch);
                }
            }
            else if (ch == '[') {
                if (!isString) {
                    res.add(new Token(Token.TokenType.ArrayBegin, "["));
                } else {
                    string.append(ch);
                }
            }
            else if (ch == ':') {
                if (!isString) {
                    res.add(new Token(Token.TokenType.Colon, ":"));
                } else {
                    string.append(ch);
                }
            }
            else if (ch == '-') {
                if (!isString) {
                    if (!isNumber) {
                        isNumber = true;
                        number.append(ch);
                    } else throw new RuntimeException("error at token build, double '-'");
                } else {
                    string.append(ch);
                }
            }
            else if (ch == '.') {
                if (!isString) {
                    if (isNumber) {
                        number.append(ch);
                    }
                } else {
                    string.append(ch);
                }
            }
            else if (Character.isLetter(ch) | ch == '_') {
                if (isString) {
                    string.append(ch);
                } else {
                    if (!isBool) {
                        isBool = true;
                    }
                    bool.append(ch);
                }
            }
            else if (Character.isDigit(ch)) {
                if (isString) {
                    string.append(ch);
                } else {
                    if (!isNumber) {
                        isNumber = true;
                    }
                    number.append(ch);
                }
            }
            else if(ch == ',') {
                if (isString) {
                    string.append(ch);
                } else {
                    if (isBool) {
                        isBool = false;
                        var tmp = bool.toString();
                        switch (tmp) {
                            case "true" -> res.add(new Token(Token.TokenType.Bool, "true"));
                            case "false" -> res.add(new Token(Token.TokenType.Bool, "false"));
                            case "null" -> res.add(new Token(Token.TokenType.Null, "null"));
                            default -> throw new RuntimeException("error in ',' ");
                        }
                        bool = new StringBuilder();
                    }
                    if (isNumber) {
                        isNumber = false;
                        res.add(new Token(Token.TokenType.Number, number.toString()));
                        number = new StringBuilder();
                    }
                    res.add(new Token(Token.TokenType.Comma, ","));
                }
            }
            else if (ch == '}') {
                if (isString) {
                    string.append(ch);
                } else {
                    if (isBool) {
                        isBool = false;
                        var tmp = bool.toString();
                        switch (tmp) {
                            case "true" -> res.add(new Token(Token.TokenType.Bool, "true"));
                            case "false" -> res.add(new Token(Token.TokenType.Bool, "false"));
                            case "null" -> res.add(new Token(Token.TokenType.Null, "null"));
                            default -> throw new RuntimeException("error in ',' ");
                        }
                        bool = new StringBuilder();
                    }
                    if (isNumber) {
                        isNumber = false;
                        res.add(new Token(Token.TokenType.Number, number.toString()));
                        number = new StringBuilder();
                    }
                    res.add(new Token(Token.TokenType.ObjectEnd, "}"));
                }
            }
            else if (ch == ']') {
                if (isString) {
                    string.append(ch);
                } else {
                    if (isBool) {
                        isBool = false;
                        var tmp = bool.toString();
                        switch (tmp) {
                            case "true" -> res.add(new Token(Token.TokenType.Bool, "true"));
                            case "false" -> res.add(new Token(Token.TokenType.Bool, "false"));
                            case "null" -> res.add(new Token(Token.TokenType.Null, "null"));
                            default -> throw new RuntimeException("error in ',' ");
                        }
                        bool = new StringBuilder();
                    }
                    if (isNumber) {
                        isNumber = false;
                        res.add(new Token(Token.TokenType.Number, number.toString()));
                        number = new StringBuilder();
                    }
                    res.add(new Token(Token.TokenType.ArrayEnd, "]"));
                }
            }
            else {
                if (isString) {
                    string.append(ch);
                }
            }
        }
        return res;
    }

    private Json() {}

    public Json get(String name) {
        var res = new Json();
        res.root = ((JsonObject)this.root).nameValueMap.get(name).t;
        return res;
    }

    public Json get(int index) {
        var res = new Json();
        res.root = ((JsonArray)this.root).get(index).t;
        return res;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
