package code.dependence.logger;

import code.app.plugin.buildin.InitNeeded;
import code.app.plugin.top.Plugin;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Plugin
@InitNeeded
public class Logger {
    private static LoggerState State;
    private static final ConcurrentLinkedQueue<LoggerMessage> messages = new ConcurrentLinkedQueue<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);
    private static final ZoneId zoneId = ZoneId.systemDefault();
    private static final List<LoggerHandle> loggerHandles = new ArrayList<>();









    // 该方法无需手动调用
    public static void Initialize() {

    }


    public static <T> LoggerHandle Register(Class<T> clazz) {


        return null;
    }

    public static void SetState(LoggerState state) {
        State = state;
    }

    public static LoggerState GetState() {
        return State;
    }
}
