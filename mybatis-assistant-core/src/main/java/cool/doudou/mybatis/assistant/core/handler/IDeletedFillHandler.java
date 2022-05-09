package cool.doudou.mybatis.assistant.core.handler;

import org.apache.ibatis.reflection.MetaObject;

/**
 * IDeletedFillHandler
 *
 * @author jiangcs
 * @since 2022/4/9
 */
public interface IDeletedFillHandler {
    /**
     * 不需要实现重写
     *
     * @param metaObject 入参对象
     */
    default void fill(MetaObject metaObject) {
        if (metaObject.hasGetter("deleted")) {
            Object tenantId = metaObject.getValue("deleted");
            if (tenantId == null) {
                metaObject.setValue("deleted", 0);
            }
        }
    }
}
