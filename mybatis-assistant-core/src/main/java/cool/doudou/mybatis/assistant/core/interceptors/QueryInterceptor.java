package cool.doudou.mybatis.assistant.core.interceptors;

import cool.doudou.mybatis.assistant.core.dialect.IDialectHandler;
import cool.doudou.mybatis.assistant.core.helper.PageHelper;
import cool.doudou.mybatis.assistant.core.page.Page;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * QueryInterceptor
 *
 * @author jiangcs
 * @since 2022/3/30
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class QueryInterceptor implements Interceptor {
    private volatile IDialectHandler dialectHandler;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        BoundSql boundSql = statementHandler.getBoundSql();
        Object parameterObject = boundSql.getParameterObject();
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        // 通过反射拿出来 BoundSql 中保存的额外参数（如果我们使用了动态 SQL，可能会存在该参数）
        Field additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
        additionalParametersField.setAccessible(true);
        Map<String, Object> additionalParameterMap = (Map<String, Object>) additionalParametersField.get(boundSql);

        String id = mappedStatement.getId();
        if (id.matches(".+Page$")) {
            Page page = PageHelper.getPage(parameterObject);
            if (page != null) {
                Connection connection = (Connection) invocation.getArgs()[0];
                // Count SQL
                BoundSql countBoundSql = dialectHandler.getCountSql(mappedStatement, parameterObject, boundSql, additionalParameterMap);
                PreparedStatement preparedStatement = connection.prepareStatement(countBoundSql.getSql());
                ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
                parameterHandler.setParameters(preparedStatement);
                ResultSet resultSet = preparedStatement.executeQuery();
                long count = 0;
                if (resultSet.next()) {
                    count = resultSet.getLong(1);
                }
                page.setTotal(count);
                // count <= 0，返回 空集合
                if (count <= 0) {
                    return Collections.emptyList();
                }
                // 分页 SQL
                boundSql = dialectHandler.getPageSql(mappedStatement, parameterObject, boundSql, additionalParameterMap, page);
            }
        }

        // 覆盖boundSql
        metaObject.setValue("delegate.boundSql", boundSql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        if (!properties.isEmpty()) {
            dialectHandler = (IDialectHandler) properties.get("dialectHandler");
        }
    }
}
