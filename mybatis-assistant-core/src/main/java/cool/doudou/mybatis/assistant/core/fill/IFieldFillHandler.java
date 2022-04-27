package cool.doudou.mybatis.assistant.core.fill;

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
     * 根据 name 填充 value
     *
     * @param metaObject metaObject
     * @param name       属性名
     * @param value      属性值
     */
    default void fill(MetaObject metaObject, String name, Object value) {
        if (metaObject.hasSetter(name)) {
            metaObject.setValue(name, value);
        }
    }
}
