package cool.doudou.mybatis.assistant.expansion.dialect;

import cool.doudou.mybatis.assistant.expansion.dialect.support.MySqlDialectHandler;
import cool.doudou.mybatis.assistant.expansion.dialect.support.OracleDialectHandler;
import cool.doudou.mybatis.assistant.expansion.dialect.support.PostgresSqlDialectHandler;
import cool.doudou.mybatis.assistant.expansion.dialect.support.SqlServerDialectHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * DialectHandlerFactory
 *
 * @author jiangcs
 * @since 2022/4/7
 */
public class DialectHandlerFactory {
    private static final Map<String, IDialectHandler> URL_DIALECT_MAP = new HashMap<>();

    static {
        // MySQL
        URL_DIALECT_MAP.put("jdbc:mysql", new MySqlDialectHandler());
        // PostgresSql
        URL_DIALECT_MAP.put("jdbc:postgresql", new PostgresSqlDialectHandler());
        // Oracle
        URL_DIALECT_MAP.put("jdbc:oracle", new OracleDialectHandler());
        // SqlServer
        URL_DIALECT_MAP.put("jdbc:sqlserver", new SqlServerDialectHandler());
    }

    public static IDialectHandler getInstance(String jdbcUrl) {
        for (String key : URL_DIALECT_MAP.keySet()) {
            if (jdbcUrl.startsWith(key)) {
                return URL_DIALECT_MAP.get(key);
            }
        }
        return null;
    }
}
