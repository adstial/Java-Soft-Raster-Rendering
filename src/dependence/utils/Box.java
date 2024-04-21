package dependence.utils;

import java.util.function.Function;

/**
 * 泛型类Box，用于封装泛型类型T的对象。
 */
public class Box<T> {
    private T t; // 封装的泛型类型对象

    /**
     * 构造函数，初始化Box对象。
     *
     * @param t 要封装的对象
     */
    public Box(T t) {
        this.t = t;
    }

    /**
     * 静态工厂方法，提供另一种创建Box对象的方式。
     *
     * @param t 要封装的对象
     * @return 返回一个新的Box实例，其中封装了传入的对象t。
     */
    public static <T> Box<T> With(T t) {
        return new Box<>(t);
    }

    public static <T> Box<T> Empty() {
        return new Box<>(null);
    }

    /**
     * 获取封装的对象。
     *
     * @return 返回封装的泛型类型对象T。
     */
    public T getT() {
        return t;
    }

    /**
     * 设置封装的对象。
     *
     * @param t 要设置的新对象。
     */
    public void setT(T t) {
        this.t = t;
    }

    /**
     * 对封装的对象应用函数式接口f，更新对象的值。
     * 该方法允许通过一个函数式接口修改封装对象的值。
     *
     * @param f 一个函数式接口，接收类型T的对象，返回类型T的结果。
     */
    public void execute(Function<T, T> f) {
        this.t = f.apply(this.t);
    }

    /**
     * 如果封装的对象不为null，则对其应用函数式接口f，更新对象的值。
     * 该方法提供了一种条件性更新对象值的机制，避免了对null的处理。
     *
     * @param f 一个函数式接口，接收类型T的对象，返回类型T的结果。
     */
    public void executeIfNotNull(Function<T, T> f) {
        if (this.t != null) {
            this.t = f.apply(this.t);
        }
    }

    /**
     * 如果封装的对象不为null，则对其应用函数式接口f，更新对象的值；如果为null，则将对象的值设置为defaultValue。
     * 该方法提供了一种条件性更新对象值的机制，并允许指定一个默认值。
     *
     * @param f 一个函数式接口，接收类型T的对象，返回类型T的结果。
     * @param defaultValue 当封装的对象为null时，用作新值的默认值。
     */
    public void executeIfNotNull(Function<T, T> f, T defaultValue) {
        if (this.t != null) {
            this.t = f.apply(this.t);
        } else {
            this.t = defaultValue;
        }
    }

    /**
     * 检查封装的对象是否为null。
     *
     * @return 如果封装的对象为null，则返回true，否则返回false。
     */
    public boolean isNull() {
        return this.t == null;
    }
}


