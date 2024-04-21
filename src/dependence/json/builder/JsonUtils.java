package dependence.json.builder;

import dependence.json.ast.Json;

public class JsonUtils {

    // TODO
    public static <T> T build(final Json json, final Class<T> clazz) {

        final var title = clazz.getDeclaredAnnotations();

        if (title.length == 0) return null;



        return null;
    }

    public static <T> T build(final String json, final Class<T> clazz) {
        return null;
    }
}
