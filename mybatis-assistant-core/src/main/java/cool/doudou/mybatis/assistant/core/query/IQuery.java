package cool.doudou.mybatis.assistant.core.query;

import java.util.Collection;

/**
 * IQuery
 *
 * @author jiangcs
 * @since 2022/4/21
 */
public interface IQuery<Child, R> {
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

    Child tenant(R column, Object tenantId);
}
