package cool.doudou.mybatiis.assistant.annotation;

import cool.doudou.mybatiis.assistant.annotation.enums.CommandTypeEnum;

import java.lang.annotation.*;

/**
 * FieldFill
 *
 * @author jiangcs
 * @since 2022/4/4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldFill {
    CommandTypeEnum fill();
}
