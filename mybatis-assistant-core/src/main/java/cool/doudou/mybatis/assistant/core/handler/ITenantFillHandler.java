package cool.doudou.mybatis.assistant.core.handler;

import org.apache.ibatis.reflection.MetaObject;

/**
 * ITenantFillHandler
 *
 * @author jiangcs
 * @since 2022/4/9
 */
public interface ITenantFillHandler {
    /**
     * @return 租户ID
     */
    Long getTenantId();

    /**
     * 不需要实现重写
     *
     * @param metaObject 入参对象
     */
    default void fill(MetaObject metaObject) {
        if (metaObject.hasGetter("tenantId")) {
            Object tenantId = metaObject.getValue("tenantId");
            if (tenantId == null) {
                metaObject.setValue("tenantId", getTenantId());
            }
        }
    }
}
