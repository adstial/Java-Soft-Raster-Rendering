package test.dependence.logger;

import code.dependence.logger.Logger;
import org.junit.Test;

public class TestUtil {
    @Test
    public void test_InfoMethodWithQuickSuccessiveCalls() {
        var log = Logger.getGlobal();
        for (int i = 0; i < 100; i++) {
            log.info("This is a long string to test the info method behavior with long input." + i);
        }
        log.waitUntilFinish();
    }

}
