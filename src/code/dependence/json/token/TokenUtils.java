package code.dependence.json.token;

import code.dependence.logger.Logger;
import code.dependence.utils.Box;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;

public class TokenUtils {
    private static final Class<TokenUtils> Self = TokenUtils.class;
    private static final Function<Integer, Integer> INF = i -> i + 1;


    /**
     * 将JSON字符串分解为标记列表。
     *
     * @param json 待分解的JSON字符串。
     * @return ArrayList<Token> 分解后的标记列表。
     */
    public static LinkedList<Token> tokenize(final String json) {
        var tokens = new LinkedList<Token>();

        // 创建一个引用，用于跟踪当前处理的字符索引
        var index = new Box<>(0);
        // 将输入字符串转换为字符数组，便于逐个字符处理
        final var chars = json.toCharArray();
        // 定义字符数组的最大索引值，即字符串的长度
        final var MAX_INDEX = chars.length;

        // 循环处理每个字符，直到处理完所有字符
        while (index.getT() < MAX_INDEX) {
            final var c = chars[index.getT()];

            // 跳过空白字符
            if (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
                index.execute(INF);
            }

            // 处理左花括号
            else if (c == '{') {
                tokens.add(new Token(TokenType.LBRACE, null));
                index.execute(INF);
            }

            // 处理右花括号
            else if (c == '}') {
                tokens.add(new Token(TokenType.RBRACE, null));
                index.execute(INF);
            }

            // 处理左方括号
            else if (c == '[') {
                tokens.add(new Token(TokenType.LBRACKET, null));
                index.execute(INF);
            }

            // 处理右方括号
            else if (c == ']') {
                tokens.add(new Token(TokenType.RBRACKET, null));
                index.execute(INF);
            }

            // 处理逗号
            else if (c == ',') {
                tokens.add(new Token(TokenType.COMMA, null));
                index.execute(INF);
            }

            // 处理冒号
            else if (c == ':') {
                tokens.add(new Token(TokenType.COLON, null));
                index.execute(INF);
            }

            // 处理字符串字面量
            else if (c == '"') {
                var value = new StringBuilder();
                index.execute(INF);

                // 循环直到找到匹配的双引号
                while (chars[index.getT()] != '"') {
                    value.append(chars[index.getT()]);
                    index.execute(INF);
                }

                tokens.add(new Token(TokenType.STRING, value.toString()));
                index.execute(INF);
            }

            // 处理true字面量
            else if (c == 't') {
                if (chars[index.getT() + 1] == 'r' && chars[index.getT() + 2] == 'u' && chars[index.getT() + 3] == 'e') {
                    tokens.add(new Token(TokenType.TRUE, null));
                    index.execute(i -> i + 4);
                }
            }

            // 处理false字面量
            else if (c == 'f') {
                if (chars[index.getT() + 1] == 'a' && chars[index.getT() + 2] == 'l' && chars[index.getT() + 3] == 's' && chars[index.getT() + 4] == 'e') {
                    tokens.add(new Token(TokenType.FALSE, null));
                    index.execute(i -> i + 5);
                }
            }

            // 处理null字面量
            else if (c == 'n') {
                if (chars[index.getT() + 1] == 'u' && chars[index.getT() + 2] == 'l' && chars[index.getT() + 3] == 'l') {
                    tokens.add(new Token(TokenType.NULL, null));
                    index.execute(i -> i + 4);
                }
            }

            // 处理数字字面量
            else if (c == '-' || Character.isDigit(c)) {
                var value = new StringBuilder();
                value.append(c);
                index.execute(INF);

                // 循环直到识别完所有数字字符
                while (Character.isDigit(chars[index.getT()]) || chars[index.getT()] == '.' || chars[index.getT()] == 'e' || chars[index.getT()] == 'E' ) {
                    value.append(chars[index.getT()]);
                    index.execute(INF);
                }

                tokens.add(new Token(TokenType.NUMBER, value.toString()));
            }
        }

        // 添加结束标记
        tokens.add(new Token(TokenType.EOF, null));

        return tokens;
    }


    public static String stringify(final Token token) {
        return switch (token.type) {
            case LBRACE -> "{";
            case RBRACE -> "}";
            case LBRACKET -> "[";
            case RBRACKET -> "]";
            case COMMA -> ",";
            case COLON -> ":";
            case STRING -> "\"" + token.value + "\"";
            case NUMBER -> token.value;
            case TRUE -> "true";
            case FALSE -> "false";
            case NULL -> "null";
            case EOF -> "E_O_F";
        };
    }

    public static String simpleStringify(final Token token) {
        return token.type.toString();
    }

    public static String simpleStringify(final ArrayList<Token> tokens) {
        return tokens.stream().map(TokenUtils::simpleStringify).reduce((a, b) -> a + " " + b).orElse("");
    }




    public static Optional<String> fromJsonFile(final String filePath) {
        try {
            return Optional.of(new String(Files.readAllBytes(Paths.get(filePath))));
        } catch (Exception ignored) {

        }
        return Optional.empty();
    }


}
