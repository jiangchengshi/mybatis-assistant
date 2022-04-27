package cool.doudou.mybatis.assistant.core.functions;

import java.io.Serializable;

/**
 * SFunction
 *
 * @author jiangcs
 * @since 2022/4/21
 */
public interface SFunction<T> extends Serializable {
    Object get(T entity);
}
