package code.dependence.logger;

public enum LoggerMessageLevel {
    FATAL(0),
    ERROR(1),
    WARNING(2),
    INFO(3),
    DEBUG(4),
    TRACE(5);

    private final int level;

    LoggerMessageLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
