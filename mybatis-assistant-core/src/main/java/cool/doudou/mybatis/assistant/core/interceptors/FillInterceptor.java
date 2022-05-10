package cool.doudou.mybatis.assistant.core.interceptors;

import cool.doudou.mybatis.assistant.core.handler.IDeletedFillHandler;
import cool.doudou.mybatis.assistant.core.handler.IFieldFillHandler;
import cool.doudou.mybatis.assistant.core.handler.IIdFillHandler;
import cool.doudou.mybatis.assistant.core.handler.ITenantFillHandler;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.Properties;

/**
 * FillInterceptor
 *
 * @author jiangcs
 * @since 2022/4/4
 */
@Intercepts(
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
)
public class FillInterceptor implements Interceptor {
    private volatile IIdFillHandler idFillHandler;
    private volatile IFieldFillHandler fieldFillHandler;
    private volatile IDeletedFillHandler deletedFillHandler;
    private volatile ITenantFillHandler tenantFillHandler;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        MetaObject metaObject = SystemMetaObject.forObject(args[1]);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (sqlCommandType == SqlCommandType.INSERT) {
            // 记录Id填充
            if (idFillHandler != null) {
                idFillHandler.fill(metaObject);
            }

            // 字段填充
            if (fieldFillHandler != null) {
                fieldFillHandler.insert(metaObject);
            }

            // 逻辑删除
            if (deletedFillHandler != null) {
                deletedFillHandler.fill(metaObject);
            }

            // 多租户填充
            if (tenantFillHandler != null) {
                tenantFillHandler.fill(metaObject);
            }
        } else if (sqlCommandType == SqlCommandType.UPDATE) {
            // 字段填充
            if (fieldFillHandler != null) {
                fieldFillHandler.update(metaObject);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        if (!properties.isEmpty()) {
            idFillHandler = (IIdFillHandler) properties.get("idFillHandler");
            fieldFillHandler = (IFieldFillHandler) properties.get("fieldFillHandler");
            deletedFillHandler = (IDeletedFillHandler) properties.get("deletedFillHandler");
            tenantFillHandler = (ITenantFillHandler) properties.get("tenantFillHandler");
        }
    }
}
