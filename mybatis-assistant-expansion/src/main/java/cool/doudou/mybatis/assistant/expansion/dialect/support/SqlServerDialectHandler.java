package cool.doudou.mybatis.assistant.expansion.dialect.support;

import cool.doudou.mybatis.assistant.expansion.dialect.IDialectHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;

/**
 * SqlServerDialectHandler
 *
 * @author jiangcs
 * @since 2022/4/7
 */
public class SqlServerDialectHandler implements IDialectHandler {
    @Override
    public BoundSql getCountSql(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql, Map<String, Object> additionalParameterMap) {
        return null;
    }

    @Override
    public BoundSql getPageSql(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql, Map<String, Object> additionalParameterMap, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public String getDriverClassName() {
        return null;
    }

    @Override
    public String getTableSql() {
        return null;
    }

    @Override
    public String getColumnSql() {
        return null;
    }
}
