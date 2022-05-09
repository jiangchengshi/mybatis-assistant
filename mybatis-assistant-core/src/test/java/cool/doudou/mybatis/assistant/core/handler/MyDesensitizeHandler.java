package cool.doudou.mybatis.assistant.core.handler;

import java.util.Map;
import java.util.function.Function;

/**
 * MyDesensitizeHandler
 *
 * @author jiangcs
 * @since 2022/5/9
 */
public class MyDesensitizeHandler implements IDesensitizeHandler {
    @Override
    public Map<String, Function<String, String>> customize() {
        return null;
    }
}
