package cool.doudou.mybatis.assistant.core.dialect.support;

import cool.doudou.mybatis.assistant.core.dialect.IDialectHandler;
import cool.doudou.mybatis.assistant.core.page.Page;
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
    public BoundSql getPageSql(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql, Map<String, Object> additionalParameterMap, Page page) {
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
