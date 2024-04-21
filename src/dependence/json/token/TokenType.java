package dependence.json.token;


public enum TokenType {
    LBRACE(0b1),     // {
    RBRACE(0b10),     // }
    LBRACKET(0b100),   // [
    RBRACKET(0b1000),   // ]
    COMMA(0b10000),      // ,
    COLON(0b100000),      // :
    STRING(0b1000000),
    NUMBER(0b10000000),
    TRUE(0b100000000),
    FALSE(0b1000000000),
    NULL(0b10000000000),
    EOF(0b100000000000);

    private final int code;

    TokenType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static int UnionTokenCode(TokenType ...types) {
        int code = 0;
        for (final var type : types) {
            code |= type.getCode();
        }
        return code;
    }
}
