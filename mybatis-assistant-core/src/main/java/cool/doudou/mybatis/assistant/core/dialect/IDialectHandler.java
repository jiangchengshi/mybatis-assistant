package cool.doudou.mybatis.assistant.core.dialect;

import cool.doudou.mybatis.assistant.core.page.Page;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;

/**
 * 数据库方言，针对不同数据库进行实现
 *
 * @author jiangcs
 * @since 2022/4/2
 */
public interface IDialectHandler {
    /**
     * 查询总数SQL
     *
     * @param mappedStatement        ms
     * @param parameterObject        param
     * @param boundSql               sql
     * @param additionalParameterMap additionalParam
     * @return BoundSql
     */
    BoundSql getCountSql(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql, Map<String, Object> additionalParameterMap);

    /**
     * 分页SQL
     *
     * @param mappedStatement        ms
     * @param parameterObject        param
     * @param boundSql               sql
     * @param additionalParameterMap additionalParam
     * @param page                   page
     * @return BoundSql
     */
    BoundSql getPageSql(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql, Map<String, Object> additionalParameterMap, Page page);

    /**
     * 数据表SQL
     *
     * @return String
     */
    String getTableSql();

    /**
     * 数据列SQL
     *
     * @return String
     */
    String getColumnSql();
}
