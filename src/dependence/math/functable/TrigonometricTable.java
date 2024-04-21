package dependence.math.functable;

import java.util.stream.IntStream;

/**
 * 三⻆函数表类，用于预计算并存储0到359度每个角度的正弦和余弦值，以便快速查询。
 */
public class TrigonometricTable{

    static {
        initialize(); // 类加载时初始化三⻆函数表
    }


    private static final double PI = Math.PI; // 圆周率PI
    private static final float[] sin = new float[360]; // 存储0到359度每个角度的正弦值
    private static final float[] cos = new float[360]; // 存储0到359度每个角度的余弦值


    /**
     * 初始化函数，计算并存储0到359度每个角度的正弦和余弦值。
     * 这个函数没有参数，也没有返回值。
     */
    public static void initialize () {
        // 对0到359度每个角度，计算其正弦和余弦值并存储到数组中
        IntStream.range(0, 360).forEach(i -> {
            sin[i] = (float) Math.sin(i * PI / 180); // 计算并存储角度i的正弦值
            cos[i] = (float) Math.cos(i * PI / 180); // 计算并存储角度i的余弦值
        });
    }

    /**
     * 根据给定的角度，返回其正弦值。
     * @param angle 角度值，必须在0到359之间。
     * @return 给定角度的正弦值。
     */
    public static float sin(int angle) {
        assert angle >= 0 && angle < 360;
        return sin[angle];
    }

    /**
     * 根据给定的角度，返回其余弦值。
     * @param angle 角度值，必须在0到359之间。
     * @return 给定角度的余弦值。
     */
    public static float cos(int angle) {
        assert angle >= 0 && angle < 360;
        return cos[angle];
    }
}

