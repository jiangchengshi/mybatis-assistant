package cool.doudou.mybatis.assistant.core.interceptors;

import cool.doudou.mybatiis.assistant.annotation.enums.CommandTypeEnum;
import cool.doudou.mybatis.assistant.core.fill.IFieldFillHandler;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.Properties;

/**
 * FieldFillInterceptor
 *
 * @author jiangcs
 * @since 2022/4/4
 */
@Intercepts(
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
)
public class FieldFillInterceptor implements Interceptor {
    private volatile IFieldFillHandler fieldFillHandler;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        MetaObject metaObject = SystemMetaObject.forObject(args[1]);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (sqlCommandType.name().equals(CommandTypeEnum.INSERT.name())) {
            fieldFillHandler.insert(metaObject);
        } else if (sqlCommandType.name().equals(CommandTypeEnum.UPDATE.name())) {
            fieldFillHandler.update(metaObject);
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
            fieldFillHandler = (IFieldFillHandler) properties.get("fieldFillHandler");
        }
    }
}
