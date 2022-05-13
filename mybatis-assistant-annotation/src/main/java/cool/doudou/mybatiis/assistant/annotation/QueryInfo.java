package cool.doudou.mybatiis.assistant.annotation;

import cool.doudou.mybatiis.assistant.annotation.enums.QueryOprEnum;

import java.lang.annotation.*;

/**
 * 查询操作符 注解
 *
 * @author jiangcs
 * @since 2022/4/4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryInfo {
    QueryOprEnum opr();
}
