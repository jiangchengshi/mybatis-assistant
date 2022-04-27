package cool.doudou.mybatis.assistant.core.query;

import cool.doudou.mybatis.assistant.core.Constant;
import cool.doudou.mybatis.assistant.core.enums.SqlKeyword;
import cool.doudou.mybatis.assistant.expansion.util.ComUtil;

import java.util.*;

/**
 * IQuery
 *
 * @author jiangcs
 * @since 2022/4/21
 */
public interface IQuery<Child, R> {
    String betweenStart = "Start", betweenEnd = "End";

    /**
     * WHERE SQL
     */
    List<String> whereList = new ArrayList<>();
    /**
     * WHERE参数值
     */
    Map<String, Object> whereParamMap = new HashMap<>();
    /**
     * ORDER BY SQL
     */
    List<String> orderByList = new ArrayList<>();
    /**
     * GROUP BY SQL
     */
    Set<String> groupBySet = new LinkedHashSet<>();
    /**
     * HAVING SQL
     */
    Set<String> havingSet = new LinkedHashSet<>();

    Child eq(R column, Object value);

    Child nEq(R column, Object value);

    Child lk(R column, Object value);

    Child lLk(R column, Object value);

    Child rLk(R column, Object value);

    Child nLk(R column, Object value);

    Child lt(R column, Object value);

    Child lte(R column, Object value);

    Child gt(R column, Object value);

    Child gte(R column, Object value);

    Child in(R column, Collection<?> value);

    Child between(R column, Object valueStart, Object valueEnd);

    Child asc(R column);

    Child desc(R column);

    Child groupBy(R column);

    Child groupBy(R... column);

    Child having(R column, String opr, Object value);

    default void where(String column, SqlKeyword sqlKeyword, Object value) {
        String propertyName = ComUtil.underline2Hump(column);
        whereList.add(Constant.BRACKETS_LEFT + String.join(Constant.SPACE, column, sqlKeyword.get(), Constant.PARAM_BRACES_LEFT + Constant.QUERY.paramName + Constant.DOT + Constant.QUERY.paramPrefix + Constant.DOT + propertyName + Constant.PARAM_BRACES_RIGHT) + Constant.BRACKETS_RIGHT);
        whereParamMap.put(propertyName, value);
    }

    default void whereBetween(String column, Object valueStart, Object valueEnd) {
        String propertyName = ComUtil.underline2Hump(column);
        whereList.add(Constant.BRACKETS_LEFT + String.join(Constant.SPACE, column, SqlKeyword.BETWEEN.get(), Constant.PARAM_BRACES_LEFT + Constant.QUERY.paramName + Constant.DOT + Constant.QUERY.paramPrefix + Constant.DOT + propertyName + betweenStart + Constant.PARAM_BRACES_RIGHT, SqlKeyword.AND.get(), Constant.PARAM_BRACES_LEFT + Constant.QUERY.paramName + Constant.DOT + Constant.QUERY.paramPrefix + Constant.DOT + propertyName + betweenEnd + Constant.PARAM_BRACES_RIGHT) + Constant.BRACKETS_RIGHT);
        whereParamMap.put(propertyName + betweenStart, valueStart);
        whereParamMap.put(propertyName + betweenEnd, valueEnd);
    }

    /**
     * xml文件中 替换的sql语句：${q.sql}
     *
     * @return sql
     */
    default String getSql() {
        StringBuilder sbQuerySql = new StringBuilder();

        // 查询
        sbQuerySql.append(SqlKeyword.WHERE.get()).append(Constant.SPACE).append(String.join(Constant.SPACE + SqlKeyword.AND.get() + Constant.SPACE, whereList));
        // 分组
        if (!groupBySet.isEmpty()) {
            sbQuerySql.append(Constant.SPACE).append(SqlKeyword.GROUP_BY.get()).append(Constant.SPACE).append(String.join(Constant.COMMA, groupBySet));

            // 分组筛选
            if (!havingSet.isEmpty()) {
                sbQuerySql.append(Constant.SPACE).append(SqlKeyword.HAVING.get()).append(Constant.SPACE).append(String.join(Constant.COMMA, havingSet));
            }
        }
        // 排序
        if (!orderByList.isEmpty()) {
            sbQuerySql.append(Constant.SPACE).append(SqlKeyword.ORDER_BY.get()).append(Constant.SPACE).append(String.join(Constant.COMMA, orderByList));
        }
        return sbQuerySql.toString();
    }

    /**
     * sql语句填充参数
     *
     * @return 参数Map
     */
    default Map<String, Object> getParam() {
        return whereParamMap;
    }
}
