package test.dependence.math;

import code.dependence.math.QuickMath;
import org.junit.Test;

public class TestMath {
    @Test
    public void test_bitInByte() {
        byte test1 = 0b0000000;
        byte test2 = 0b0101010;
        byte test3 = 0b1010100;

        for (int i = 0; i < 7; i++) {
            assert !QuickMath.bitInByte(test1, (byte) i);
        }

        assert !QuickMath.bitInByte(test2, 0);
        assert QuickMath.bitInByte(test3, 2);
    }
}
