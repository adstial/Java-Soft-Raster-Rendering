package code.dependence.serialization.json_paser;

import java.util.ArrayList;

public class Token {
    public enum TokenType {
        ObjectBegin,
        ObjectEnd,
        ArrayBegin,
        ArrayEnd,
        String,
        Number,
        Bool,
        Null,
        Comma,// ,
        Colon,// :
    }
    public TokenType type;
    public String value;
    public Token (TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public static ArrayList<Token> stringToTokenList(final String context) {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Token\t" +
                "type= " + type  +
                ", value= " + value;
    }
}
