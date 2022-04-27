package cool.doudou.mybatis.assistant.core.dialect.support;

import cool.doudou.mybatis.assistant.core.dialect.IDialectHandler;
import cool.doudou.mybatis.assistant.core.page.Page;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;
import java.util.Set;

/**
 * MySqlDialectHandler
 *
 * @author jiangcs
 * @since 2022/4/2
 */
public class MySqlDialectHandler implements IDialectHandler {
    @Override
    public BoundSql getCountSql(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql, Map<String, Object> additionalParameterMap) {
        String countSql = "SELECT COUNT(*) FROM (" + boundSql.getSql() + ") TMP";
        // todo 去掉 order by
        // todo 优化 left join
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);

        // 参数
        Set<String> keySet = additionalParameterMap.keySet();
        for (String key : keySet) {
            countBoundSql.setAdditionalParameter(key, additionalParameterMap.get(key));
        }
        return countBoundSql;
    }

    @Override
    public BoundSql getPageSql(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql, Map<String, Object> additionalParameterMap, Page page) {
        // offset，count
        int offset = (page.getPageNum() - 1) * page.getPageSize();
        int count = page.getPageSize();

        String pageSql = "SELECT * FROM (" + boundSql.getSql() + " LIMIT " + offset + "," + count + ") TMP";
        BoundSql pageBoundSql = new BoundSql(mappedStatement.getConfiguration(), pageSql, boundSql.getParameterMappings(), parameterObject);

        // 参数
        Set<String> keySet = additionalParameterMap.keySet();
        for (String key : keySet) {
            pageBoundSql.setAdditionalParameter(key, additionalParameterMap.get(key));
        }
        return pageBoundSql;
    }

    @Override
    public String getTableSql() {
        return "SELECT TABLE_COMMENT FROM TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
    }

    @Override
    public String getColumnSql() {
        return "SELECT COLUMN_NAME,DATA_TYPE,COLUMN_COMMENT,COLUMN_KEY FROM COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
    }
}
