package dependence.json.ast;

import dependence.json.exception.NoTokenException;
import dependence.json.exception.UnExpectException;
import dependence.json.token.TokenList;
import dependence.json.token.TokenType;

import static dependence.json.token.TokenType.UnionTokenCode;

public class Json {

    public boolean OBJECT_OR_ARRAY;
    public static final boolean OBJECT = true;
    public static final boolean ARRAY = false;

    public Object value;

    public Json(final TokenList tokens) {
        var expectToken = UnionTokenCode(TokenType.LBRACE, TokenType.LBRACKET);

        if (tokens == null || tokens.isEmpty()) {
            throw new NoTokenException("No token");
        }

        final var token = tokens.removeFirst();
        if (token.type() == TokenType.LBRACE) {
            OBJECT_OR_ARRAY = OBJECT;
        } else if (token.type() == TokenType.LBRACKET) {
            OBJECT_OR_ARRAY = ARRAY;
        } else {
            throw new UnExpectException("Invalid Json");
        }

    }
}
