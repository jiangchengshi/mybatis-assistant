package cool.doudou.mybatis.assistant.core.query;

import cool.doudou.mybatis.assistant.core.common.Constant;
import cool.doudou.mybatis.assistant.core.enums.SqlKeyword;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * 查询实体
 *
 * @author jiangcs
 * @since 2022/4/20
 */
public class Query<T> extends BaseQuery implements IQuery<Query<T>, String> {
    public Query() {
        this.clear();

        this.where("deleted", SqlKeyword.EQ, 0);
    }

    public Query(T t) {
        this.clear();

        this.where("deleted", SqlKeyword.EQ, 0);

        this.assign(t);
    }

    @Override
    public Query<T> eq(String column, Object value) {
        this.where(column, SqlKeyword.EQ, value);
        return this;
    }

    @Override
    public Query<T> nEq(String column, Object value) {
        this.where(column, SqlKeyword.NOT_EQ, value);
        return this;
    }

    @Override
    public Query<T> lk(String column, Object value) {
        this.where(column, SqlKeyword.LIKE, Constant.PERCENT + value + Constant.PERCENT);
        return this;
    }

    @Override
    public Query<T> lLk(String column, Object value) {
        this.where(column, SqlKeyword.LIKE, Constant.PERCENT + value);
        return this;
    }

    @Override
    public Query<T> rLk(String column, Object value) {
        this.where(column, SqlKeyword.LIKE, value + Constant.PERCENT);
        return this;
    }

    @Override
    public Query<T> nLk(String column, Object value) {
        this.where(column, SqlKeyword.NOT_LIKE, Constant.PERCENT + value + Constant.PERCENT);
        return this;
    }

    @Override
    public Query<T> lt(String column, Object value) {
        this.where(column, SqlKeyword.LT, value);
        return this;
    }

    @Override
    public Query<T> lte(String column, Object value) {
        this.where(column, SqlKeyword.LTE, value);
        return this;
    }

    @Override
    public Query<T> gt(String column, Object value) {
        this.where(column, SqlKeyword.GT, value);
        return this;
    }

    @Override
    public Query<T> gte(String column, Object value) {
        this.where(column, SqlKeyword.GTE, value);
        return this;
    }

    @Override
    public Query<T> in(String column, Collection<?> value) {
        this.where(column, SqlKeyword.IN, value);
        return this;
    }

    @Override
    public Query<T> between(String column, Object valueStart, Object valueEnd) {
        this.whereBetween(column, valueStart, valueEnd);
        return this;
    }

    @Override
    public Query<T> asc(String column) {
        this.orderBy(column, SqlKeyword.ASC);
        return this;
    }

    @Override
    public Query<T> desc(String column) {
        this.orderBy(column, SqlKeyword.DESC);
        return this;
    }

    @Override
    public Query<T> groupBy(String column) {
        this.groupBy(new HashSet<>(List.of(column)));
        return this;
    }

    @Override
    public Query<T> groupBy(String... columns) {
        this.groupBy(new HashSet<>(Arrays.asList(columns)));
        return this;
    }

    @Override
    public Query<T> having(String column, String opr, Object value) {
        this.havingBy(column, opr, value);
        return this;
    }

    @Override
    public Query<T> tenant(String column, Object tenantId) {
        this.where(column, SqlKeyword.EQ, tenantId);
        return this;
    }
}
