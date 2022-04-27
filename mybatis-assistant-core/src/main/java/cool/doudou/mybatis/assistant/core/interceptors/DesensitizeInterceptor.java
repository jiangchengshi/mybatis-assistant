package cool.doudou.mybatis.assistant.core.interceptors;

import cool.doudou.mybatiis.assistant.annotation.Desensitize;
import cool.doudou.mybatiis.assistant.annotation.enums.StrategyEnum;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * DesensitizeInterceptor
 *
 * @author jiangcs
 * @since 2022/4/4
 */
@Intercepts(
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = Statement.class)
)
public class DesensitizeInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        List<Object> list = (List<Object>) invocation.proceed();
        list.forEach(this::process);
        return list;
    }

    private void process(Object source) {
        MetaObject metaObject = SystemMetaObject.forObject(source);
        Arrays.stream(source.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Desensitize.class))
                .forEach(f -> {
                    String name = f.getName();
                    Object value = metaObject.getValue(name);
                    if (metaObject.getGetterType(name) == String.class && value != null) {
                        Desensitize desensitize = f.getAnnotation(Desensitize.class);
                        StrategyEnum strategy = desensitize.strategy();
                        metaObject.setValue(name, strategy.getFunction().apply(String.valueOf(value)));
                    }
                });
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
