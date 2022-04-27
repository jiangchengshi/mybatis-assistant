package cool.doudou.mybatis.assistant.core.query;

import cool.doudou.mybatis.assistant.core.Constant;
import cool.doudou.mybatis.assistant.core.enums.SqlKeyword;

import java.util.Arrays;
import java.util.Collection;

/**
 * 查询实体
 *
 * @author jiangcs
 * @since 2022/4/20
 */
public class Query implements IQuery<Query, String> {
    @Override
    public Query eq(String column, Object value) {
        this.where(column, SqlKeyword.EQ, value);
        return this;
    }

    @Override
    public Query nEq(String column, Object value) {
        this.where(column, SqlKeyword.NOT_EQ, value);
        return this;
    }

    @Override
    public Query lk(String column, Object value) {
        this.where(column, SqlKeyword.LIKE, Constant.PERCENT + value + Constant.PERCENT);
        return this;
    }

    @Override
    public Query lLk(String column, Object value) {
        this.where(column, SqlKeyword.LIKE, Constant.PERCENT + value);
        return this;
    }

    @Override
    public Query rLk(String column, Object value) {
        this.where(column, SqlKeyword.LIKE, value + Constant.PERCENT);
        return this;
    }

    @Override
    public Query nLk(String column, Object value) {
        this.where(column, SqlKeyword.NOT_LIKE, Constant.PERCENT + value + Constant.PERCENT);
        return this;
    }

    @Override
    public Query lt(String column, Object value) {
        this.where(column, SqlKeyword.LT, value);
        return this;
    }

    @Override
    public Query lte(String column, Object value) {
        this.where(column, SqlKeyword.LTE, value);
        return this;
    }

    @Override
    public Query gt(String column, Object value) {
        this.where(column, SqlKeyword.GT, value);
        return this;
    }

    @Override
    public Query gte(String column, Object value) {
        this.where(column, SqlKeyword.GTE, value);
        return this;
    }

    @Override
    public Query in(String column, Collection<?> value) {
        this.where(column, SqlKeyword.IN, value);
        return this;
    }

    @Override
    public Query between(String column, Object valueStart, Object valueEnd) {
        this.whereBetween(column, valueStart, valueEnd);
        return this;
    }

    @Override
    public Query asc(String column) {
        orderByList.add(String.join(Constant.SPACE, column, SqlKeyword.ASC.get()));
        return this;
    }

    @Override
    public Query desc(String column) {
        orderByList.add(String.join(Constant.SPACE, column, SqlKeyword.DESC.get()));
        return this;
    }

    @Override
    public Query groupBy(String column) {
        groupBySet.add(column);
        return this;
    }

    @Override
    public Query groupBy(String... column) {
        groupBySet.addAll(Arrays.asList(column));
        return this;
    }

    @Override
    public Query having(String column, String opr, Object value) {
        havingSet.add(String.join(Constant.SPACE, column, opr, String.valueOf(value)));
        return this;
    }
}
