package cool.doudou.mybatiis.assistant.annotation.enums;

import java.util.function.Function;

/**
 * 脱敏策略 枚举
 *
 * @author jiangcs
 * @since 2022/4/4
 */
public enum StrategyEnum {
    /**
     * 用户名
     */
    USER_NAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2"));

    private final Function<String, String> function;

    StrategyEnum(Function<String, String> function) {
        this.function = function;
    }

    public Function<String, String> getFunction() {
        return function;
    }
}
