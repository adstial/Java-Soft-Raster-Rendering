package code.dependence.VBO_builder;

import code.dependence.logger.Logger;

public class VBOBuilderFactory {
    public static <T extends VBOBuilder<?,?>> T SetStyle(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Logger.getGlobal().fatal(VBOBuilderFactory.class, "FAIL TO LOAD VBOBuilder");
            throw new RuntimeException();
        }
    }
}
