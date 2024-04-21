package dependence.json.token;

public record Token(TokenType type, String value) {
    public String toString() {
        return "Token(" + type + ", " + value + ")";
    }

    public TokenType getTokenType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
