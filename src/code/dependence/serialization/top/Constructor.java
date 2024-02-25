package code.dependence.serialization.top;

import code.dependence.logger.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class Constructor {
    public static enum Type {
        File,
        String
    }
    public static <T extends Constructor> T from(final Class<T> clazz, final Type type, final String name) {
        try {
            var obj = clazz.getDeclaredConstructor().newInstance();

            return obj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger.getGlobal().fatal(Constructor.class, "error in Constructor" + e);
            throw new RuntimeException(e);
        }
    }

    public Constructor(final Type type, final String name) {}
}
