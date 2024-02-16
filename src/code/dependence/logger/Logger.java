package code.dependence.logger;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class Logger {
    static Logger globalLogger;
    public static Logger getGlobal() {
        if (globalLogger == null) {
            globalLogger = new Logger();
        }
        return globalLogger;
    }
    private Logger() {
    }

    enum ConsoleColor {
        RESET("\u001B[0m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        BLUE("\u001B[34m");
        private final String code;
        ConsoleColor(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    private static String getTimeString() {
        var time = LocalDateTime.now();
        var hour = time.getHour();
        String t;
        if (hour > 12) t = "下午";
        else t = "上午";
        return String.format("%d-%d-%d %s %d:%d:%d",time.getYear(), time.getMonth().getValue(), time.getDayOfMonth(), t, hour, time.getMinute(), time.getSecond());
    }

    private static final String infoSign = ConsoleColor.BLUE.getCode() + "[INFO]\t" + ConsoleColor.RESET.getCode();
    public void info(String info) {
        if (future == null)
            future = printColoredTextAsync(info);
        else
            future = future.thenComposeAsync((VOID) -> printColoredTextAsync(info));
    }
    private CompletableFuture<Void> future;

    public static CompletableFuture<Void> printColoredTextAsync(String text) {
        return CompletableFuture.runAsync(() -> {
            System.out.println(getTimeString() + "  " + infoSign + text);
        });
    }

    public void waitUntilFinish() {
        CompletableFuture.allOf(future).join();
    }
}
