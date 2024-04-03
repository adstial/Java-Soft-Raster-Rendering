package code.dependence.utils;

import java.util.function.Function;

public class Box<T> {
    private T t;
    public Box(T t) {
        this.t = t;
    }
    public static <T> Box<T> With(T t) {
        return new Box<>(t);
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public void execute(Function<T, T> f) {
        this.t = f.apply(this.t);
    }
}
