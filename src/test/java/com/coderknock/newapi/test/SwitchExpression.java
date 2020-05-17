package com.coderknock.newapi.test;

/**
 * @author sanchan
 * @date 2020-05-02
 * @since 1.0.0
 */
public class SwitchExpression {
    public static void main(String[] args) {
        Season type = Season.SSS;
        String s = switch (type) {
            case SPRING -> "春";
            case SUMMER -> "夏";
            case AUTUMN -> "秋";
            case WINTER -> "冬";
            default -> {
                System.out.println("没有" + type + "这个选项");
                // return 会直接跳出当前循环或者方法，而 yield 只会跳出当前 switch 块。
                yield "error";
            }
        };
        System.out.println(s);
    }

    enum Season {
        SPRING, SUMMER, AUTUMN, WINTER, SSS
    }
}
