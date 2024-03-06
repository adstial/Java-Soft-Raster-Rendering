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
        return String.format("%d-%d-%d %s %d:%d:%d\t",time.getYear(), time.getMonth().getValue(), time.getDayOfMonth(), t, hour, time.getMinute(), time.getSecond());
    }


    private CompletableFuture<Void> future;

    private static final String infoSign = ConsoleColor.BLUE.getCode() + "[INFO]\t" + ConsoleColor.RESET.getCode();
    public <T> void info(Class<T> clazz, String info) {
        if (future == null)
            future = AsyncPrintlnWithTime(infoSign + clazz.getSimpleName() + ": " + info);
        else
            future = future.thenComposeAsync((VOID) -> AsyncPrintlnWithTime(infoSign + clazz.getSimpleName() + ": " + info));

    }


    public void info(String info) {
        if (future == null)
            future = AsyncPrintlnWithTime(infoSign + info);
        else
            future = future.thenComposeAsync((VOID) -> AsyncPrintlnWithTime(infoSign + info));
    }



    
    
    private static final String fatalSign = ConsoleColor.RED.getCode() + "[FATAL]\t" + ConsoleColor.RESET.getCode();

    public <T> void fatal(Class<T> clazz, String fatal) {
        if (future == null)
            future = AsyncPrintlnWithTime(fatalSign + clazz.getSimpleName() + ": " + fatal);
        else
            future = future.thenComposeAsync((VOID) -> AsyncPrintlnWithTime(fatalSign + clazz.getSimpleName() + ": " + fatal));
    }


    public void fatal(String fatal) {
        if (future == null)
            future = AsyncPrintlnWithTime(fatalSign + fatal);
        else
            future = future.thenComposeAsync((VOID) -> AsyncPrintlnWithTime(fatalSign + fatal));
    }





    private static final String warmingSign =
            ConsoleColor.GREEN.getCode() + "[WARMING]\t" + ConsoleColor.RESET.getCode();

    public <T> void warming(Class<T> clazz, String warming) {
        if (future == null)
            future = AsyncPrintlnWithTime(warmingSign + clazz.getSimpleName() + ": " + warming);
        else
            future = future.thenComposeAsync((VOID) -> AsyncPrintlnWithTime(warmingSign + clazz.getSimpleName() + ": " + warming));
    }


    public void warming(String warming) {
        if (future == null)
            future = AsyncPrintlnWithTime(warmingSign + warming);
        else
            future = future.thenComposeAsync((VOID) -> AsyncPrintlnWithTime(warmingSign + warming));
    }




    private static CompletableFuture<Void> AsyncPrintlnWithTime(String text) {
        return CompletableFuture.runAsync(() -> System.out.println(getTimeString() + text));
    }


    public void waitUntilFinish() {
        if (future != null) CompletableFuture.allOf(future).join();
    }
}
