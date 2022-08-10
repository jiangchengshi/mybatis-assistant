package cool.doudou.mybatis.assistant.expansion.dialect;

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
     * @param pageNum                当前页码
     * @param pageSize               每页条数
     * @return BoundSql
     */
    BoundSql getPageSql(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql, Map<String, Object> additionalParameterMap, int pageNum, int pageSize);
}
