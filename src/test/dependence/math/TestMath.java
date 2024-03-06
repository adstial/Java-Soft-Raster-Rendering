package test.dependence.math;

import code.dependence.math.QuickMath;
import org.junit.Test;

public class TestMath {
    @Test
    public void test_divideNumbers() {
        int n = 1000;
        int k = 4;

        // 创建一个二维数组用于存储结果
        int[][] result = new int[k][2];

        // 调用divideNumbers函数，传入n、k以及结果数组
        divideNumbers(n, k, result);

        // 打印结果
        for (int i = 0; i < result.length; i++) {
            System.out.println("[" + result[i][0] + ", " + result[i][1] + "]");
        }
    }

    public void divideNumbers(int n, int k, int[][] res) {
        int quotient = n / k;
        int remainder = n % k;

        int start = 0;
        int end = 0;

        for (int i = 0; i < k; i++) {
            // 计算当前组的结束索引
            end = start + quotient + (i < remainder ? 1 : 0);

            // 将开始和结束索引存储在结果数组中
            res[i][0] = start;
            res[i][1] = end - 1;

            // 更新下一次迭代的开始索引
            start = end;
        }
    }


    @Test
    public void test() {
        System.out.println((int) ((1f / 60) * 1000));
    }
}
