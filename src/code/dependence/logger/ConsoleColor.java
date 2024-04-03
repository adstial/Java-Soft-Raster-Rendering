package code.dependence.logger;

public enum ConsoleColor {
    // 为每个枚举值添加文档注释，说明其用途
    RESET("\u001B[0m"),     // 重置所有属性到默认值
    RED("\u001B[31m"),      // 红色文本
    GREEN("\u001B[32m"),    // 绿色文本
    YELLOW("\u001B[33m"),   // 黄色文本
    PURPLE("\u001B[35m"),   // 紫色文本
    BLACK("\u001B[30m"),    // 黑色文本
    WHITE("\u001B[37m"),    // 白色文本
    BLUE("\u001B[34m"),     // 蓝色文本
    CYAN("\u001B[36m");    // 青色文本
    private final String code;

    ConsoleColor(final String code) {
        this.code = code;
    }

    /**
     * 获取颜色代码字符串
     * @return 表示颜色的ANSI转义序列
     */
    public String getCode() {
        return code;
    }

}

