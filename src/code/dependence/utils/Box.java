package code.dependence.utils;

public final class Box<T> {
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
}
