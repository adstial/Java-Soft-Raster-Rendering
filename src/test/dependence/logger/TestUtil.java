package test.dependence.logger;

import code.dependence.logger.Logger;
import org.junit.Test;

public class TestUtil {
    @Test
    public void test_InfoMethodWithQuickSuccessiveCalls() {
        var log = Logger.getGlobal();
        var nowTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            log.info("This is a long string to test the info method behavior with long input." + i);
        }
        var dt = System.currentTimeMillis() - nowTime;
        System.out.println(dt);

        System.out.println("other thing");
        log.waitUntilFinish();
    }

    @Test
    public void test_Fatal() {
        var log = Logger.getGlobal();
        log.fatal("fail to read file");
        log.waitUntilFinish();
    }

}
