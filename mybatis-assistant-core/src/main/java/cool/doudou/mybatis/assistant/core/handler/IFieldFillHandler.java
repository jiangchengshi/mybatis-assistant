package cool.doudou.mybatis.assistant.core.handler;

import org.apache.ibatis.reflection.MetaObject;

/**
 * IFieldFillHandler
 *
 * @author jiangcs
 * @since 2022/4/4
 */
public interface IFieldFillHandler {
    /**
     * 插入时 填充处理器
     *
     * @param metaObject metaObject
     */
    void insert(MetaObject metaObject);

    /**
     * 更新时 填充处理器
     *
     * @param metaObject metaObject
     */
    void update(MetaObject metaObject);

    /**
     * 不需要实现重写
     *
     * @param metaObject 入参对象
     * @param name       属性名
     * @param value      属性值
     */
    default void fill(MetaObject metaObject, String name, Object value) {
        if (metaObject.hasSetter(name)) {
            Object valueExists = metaObject.getValue(name);
            if (valueExists == null) {
                metaObject.setValue(name, value);
            }
        }
    }
}
