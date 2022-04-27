package cool.doudou.mybatiis.assistant.annotation;

import cool.doudou.mybatiis.assistant.annotation.enums.StrategyEnum;

import java.lang.annotation.*;

/**
 * 脱敏 注解
 *
 * @author jiangcs
 * @since 2022/4/4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Desensitize {
    StrategyEnum strategy();
}
