package cool.doudou.mybatis.assistant.core.interceptors;

import cool.doudou.mybatis.assistant.annotation.Desensitize;
import cool.doudou.mybatis.assistant.core.handler.IDesensitizeHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Statement;
import java.util.*;
import java.util.function.Function;

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
    private static final Map<String, Function<String, String>> STRATEGY_MAP = new HashMap<>();

    static {
        // 用户名前一后一
        STRATEGY_MAP.put("userName", s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2"));
        // 身份证前三后四
        STRATEGY_MAP.put("idCard", s -> s.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
        // 手机号码前三后四
        STRATEGY_MAP.put("phoneNumber", s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        List<Object> list = (List<Object>) invocation.proceed();
        if (list != null && !list.isEmpty()) {
            list.forEach(this::process);
        }
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
                        Function<String, String> desensitizeFunction = STRATEGY_MAP.get(desensitize.strategy());
                        if (desensitizeFunction != null) {
                            metaObject.setValue(name, desensitizeFunction.apply(String.valueOf(value)));
                        }
                    }
                });
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        if (!properties.isEmpty()) {
            IDesensitizeHandler desensitizeHandler = (IDesensitizeHandler) properties.get("desensitizeHandler");
            if (desensitizeHandler != null) {
                STRATEGY_MAP.putAll(desensitizeHandler.customize());
            }
        }
    }
}
