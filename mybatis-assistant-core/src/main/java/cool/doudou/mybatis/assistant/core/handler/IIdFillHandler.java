package cool.doudou.mybatis.assistant.core.handler;

import cool.doudou.mybatis.assistant.expansion.util.IdUtil;
import org.apache.ibatis.reflection.MetaObject;

/**
 * IIdFillHandler
 *
 * @author jiangcs
 * @since 2022/4/9
 */
public interface IIdFillHandler {
    Long nextId();

    /**
     * 不需要实现重写
     *
     * @param metaObject 入参对象
     */
    default void fill(MetaObject metaObject) {
        if (metaObject.hasGetter("id")) {
            Object id = metaObject.getValue("id");
            if (id == null) {
                metaObject.setValue("id", nextId() != null ? nextId() : IdUtil.nextId());
            }
        }
    }
}
