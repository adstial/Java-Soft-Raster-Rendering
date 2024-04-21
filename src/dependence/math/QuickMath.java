package dependence.math;

public class QuickMath {
    public static float invSqrt(float x) {
        float xhalf = 0.5f * x;
        int i = Float.floatToIntBits(x);
        i = 0x5f3759df - (i >> 1);
        x = Float.intBitsToFloat(i);
        x *= (1.5f - xhalf * x * x);
        return x;
    }

    public static void ave(int n, int k, final int[][] res) {
        assert n >= 0; assert k > 0;
        var quotient = n / k;
        var remainder = n % k;

        int start = 0;
        int end = 0;

        for (int i = 0; i < k; i++) {
            end = start + quotient + (i < remainder ? 1 : 0);

            res[i][0] = start;
            res[i][1] = end - 1;

            start = end;
        }

    }
}
