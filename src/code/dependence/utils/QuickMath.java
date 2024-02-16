package code.dependence.utils;

public class QuickMath {
    public static float[] sin;
    public static float[] cos;

    public static void initialize() {
        //产生一个用于快速获得三角函数值的查找表
        sin = new float[1+360*2];
        cos = new float[1+360*2];
        for(int i = 0; i < sin.length; i ++) {
            sin[i] = (float)Math.sin(Math.PI*(i-360)/180);
            cos[i] = (float)Math.cos(Math.PI*(i-360)/180);
        }
    }
    public static float invSqrt(float x) {
        float xhalf = 0.5f * x;
        int i = Float.floatToIntBits(x);
        i = 0x5f3759df - (i >> 1);
        x = Float.intBitsToFloat(i);
        x *= (1.5f - xhalf * x * x);
        return x;
    }
    public static float getSin(int rate) {
        if (rate >= -360 && rate <= 360) return sin[rate+360];
        else throw new RuntimeException("wrong:" + rate);
    }
    public static float getCos(int rate) {
        if (rate >= -360 && rate <= 360) return cos[rate+360];
        else throw new RuntimeException("wrong:" + rate);
    }
}
