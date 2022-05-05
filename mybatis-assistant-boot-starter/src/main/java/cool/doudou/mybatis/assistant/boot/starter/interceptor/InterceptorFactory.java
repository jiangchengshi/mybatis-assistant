package cool.doudou.mybatis.assistant.boot.starter.interceptor;

import cool.doudou.mybatis.assistant.boot.starter.Constant;
import cool.doudou.mybatis.assistant.core.interceptors.DesensitizeInterceptor;
import cool.doudou.mybatis.assistant.core.interceptors.FieldFillInterceptor;
import cool.doudou.mybatis.assistant.core.interceptors.QueryInterceptor;
import org.apache.ibatis.plugin.Interceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * InterceptorFactory
 *
 * @author jiangcs
 * @since 2022/4/7
 */
public class InterceptorFactory {
    private static final Map<String, Interceptor> INTERCEPTOR_MAP = new HashMap<>();

    static {
        // 分页
        INTERCEPTOR_MAP.put(Constant.InterceptorName.QUERY, new QueryInterceptor());
        // 字段填充
        INTERCEPTOR_MAP.put(Constant.InterceptorName.FIELD_FILL, new FieldFillInterceptor());
        // 脱敏
        INTERCEPTOR_MAP.put(Constant.InterceptorName.DESENSITIZE, new DesensitizeInterceptor());
    }

    public static Interceptor getInstance(String name) {
        for (String key : INTERCEPTOR_MAP.keySet()) {
            if (name.equals(key)) {
                return INTERCEPTOR_MAP.get(key);
            }
        }
        return null;
    }
}
