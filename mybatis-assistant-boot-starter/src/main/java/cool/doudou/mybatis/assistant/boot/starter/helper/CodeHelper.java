package cool.doudou.mybatis.assistant.boot.starter.helper;

import cool.doudou.mybatis.assistant.generator.gen.Generator;

/**
 * CodeHelper
 *
 * @author jiangcs
 * @since 2022/07/26
 */
public class CodeHelper {
    public Generator build(String ip, int port, String user, String password) {
        return new Generator(ip, port, user, password);
    }
}
