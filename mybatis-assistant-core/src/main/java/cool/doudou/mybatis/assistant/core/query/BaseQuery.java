package cool.doudou.mybatis.assistant.core.query;

import cool.doudou.mybatiis.assistant.annotation.QueryInfo;
import cool.doudou.mybatiis.assistant.annotation.enums.QueryOprEnum;
import cool.doudou.mybatis.assistant.core.common.Constant;
import cool.doudou.mybatis.assistant.core.enums.SqlKeyword;
import cool.doudou.mybatis.assistant.expansion.util.ComUtil;

import java.util.*;

/**
 * BaseQuery
 *
 * @author jiangcs
 * @since 2022/5/14
 */
public class BaseQuery {
    /**
     * WHERE SQL
     */
    private final List<String> whereList = new ArrayList<>();
    /**
     * WHERE参数值
     */
    private final Map<String, Object> whereParamMap = new HashMap<>();
    /**
     * ORDER BY SQL
     */
    private final List<String> orderByList = new ArrayList<>();
    /**
     * GROUP BY SQL
     */
    private final Set<String> groupBySet = new LinkedHashSet<>();
    /**
     * HAVING SQL
     */
    private final Set<String> havingSet = new LinkedHashSet<>();

    protected void clear() {
        whereList.clear();
        whereParamMap.clear();
        orderByList.clear();
        groupBySet.clear();
        havingSet.clear();
    }

    protected void where(String column, SqlKeyword sqlKeyword, Object value) {
        String propertyName = ComUtil.underline2Hump(column);
        whereList.add(Constant.BRACKETS_LEFT + String.join(Constant.SPACE, column, sqlKeyword.get(), Constant.PARAM_BRACES_LEFT + Constant.QUERY.paramName + Constant.DOT + Constant.QUERY.paramPrefix + Constant.DOT + propertyName + Constant.PARAM_BRACES_RIGHT) + Constant.BRACKETS_RIGHT);
        whereParamMap.put(propertyName, value);
    }

    protected void whereBetween(String column, Object valueStart, Object valueEnd) {
        String propertyName = ComUtil.underline2Hump(column);
        String betweenStart = "Start", betweenEnd = "End";
        whereList.add(Constant.BRACKETS_LEFT + String.join(Constant.SPACE, column, SqlKeyword.BETWEEN.get(), Constant.PARAM_BRACES_LEFT + Constant.QUERY.paramName + Constant.DOT + Constant.QUERY.paramPrefix + Constant.DOT + propertyName + betweenStart + Constant.PARAM_BRACES_RIGHT, SqlKeyword.AND.get(), Constant.PARAM_BRACES_LEFT + Constant.QUERY.paramName + Constant.DOT + Constant.QUERY.paramPrefix + Constant.DOT + propertyName + betweenEnd + Constant.PARAM_BRACES_RIGHT) + Constant.BRACKETS_RIGHT);
        whereParamMap.put(propertyName + betweenStart, valueStart);
        whereParamMap.put(propertyName + betweenEnd, valueEnd);
    }

    protected void orderBy(String column, SqlKeyword sqlKeyword) {
        this.orderByList.add(String.join(Constant.SPACE, column, sqlKeyword.get()));
    }

    protected void groupBy(Set<String> columns) {
        this.groupBySet.addAll(columns);
    }

    protected void havingBy(String column, String opr, Object value) {
        this.havingSet.add(String.join(Constant.SPACE, column, opr, String.valueOf(value)));
    }

    protected <T> void assign(T t) {
        if (t != null) {
            Arrays.stream(t.getClass().getDeclaredFields()).forEach(field -> {
                try {
                    if (field.isAnnotationPresent(QueryInfo.class)) {
                        field.setAccessible(true);

                        Object value = field.get(t);
                        if (value != null) {
                            String column = ComUtil.hump2Underline(field.getName());

                            QueryInfo queryInfo = field.getDeclaredAnnotation(QueryInfo.class);
                            QueryOprEnum queryOprEnum = queryInfo.opr();
                            switch (queryOprEnum) {
                                case EQ:
                                    where(column, SqlKeyword.EQ, value);
                                    break;
                                case NOT_EQ:
                                    where(column, SqlKeyword.NOT_EQ, value);
                                    break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * xml文件中 替换的sql语句：${q.sql}
     *
     * @return sql
     */
    protected String getSql() {
        StringBuilder sbQuerySql = new StringBuilder();

        // 查询
        if (!whereList.isEmpty()) {
            sbQuerySql.append(SqlKeyword.WHERE.get()).append(Constant.SPACE).append(String.join(Constant.SPACE + SqlKeyword.AND.get() + Constant.SPACE, whereList));
        }
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
    protected Map<String, Object> getParam() {
        return whereParamMap;
    }
}
