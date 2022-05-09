package cool.doudou.mybatis.assistant.core.handler;

import java.util.Map;
import java.util.function.Function;

/**
 * ITenantFillHandler
 *
 * @author jiangcs
 * @since 2022/4/9
 */
public interface IDesensitizeHandler {
    /**
     * 自定义脱敏策略
     *
     * @return 脱敏策略Map
     */
    Map<String, Function<String, String>> customize();
}
