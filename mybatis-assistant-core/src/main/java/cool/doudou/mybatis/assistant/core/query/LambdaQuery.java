package cool.doudou.mybatis.assistant.core.query;

import cool.doudou.mybatis.assistant.core.Constant;
import cool.doudou.mybatis.assistant.core.enums.SqlKeyword;
import cool.doudou.mybatis.assistant.core.functions.FunctionGetter;
import cool.doudou.mybatis.assistant.core.functions.SFunction;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Lambda查询实体
 *
 * @author jiangcs
 * @since 2022/4/20
 */
public class LambdaQuery<T> implements IQuery<LambdaQuery<T>, SFunction<T>> {
    @Override
    public LambdaQuery<T> eq(SFunction<T> column, Object value) {
        this.where(FunctionGetter.name(column), SqlKeyword.EQ, value);
        return this;
    }

    @Override
    public LambdaQuery<T> nEq(SFunction<T> column, Object value) {
        this.where(FunctionGetter.name(column), SqlKeyword.NOT_EQ, value);
        return this;
    }

    @Override
    public LambdaQuery<T> lk(SFunction<T> column, Object value) {
        this.where(FunctionGetter.name(column), SqlKeyword.LIKE, Constant.PERCENT + value + Constant.PERCENT);
        return this;
    }

    @Override
    public LambdaQuery<T> lLk(SFunction<T> column, Object value) {
        this.where(FunctionGetter.name(column), SqlKeyword.LIKE, Constant.PERCENT + value);
        return this;
    }

    @Override
    public LambdaQuery<T> rLk(SFunction<T> column, Object value) {
        this.where(FunctionGetter.name(column), SqlKeyword.LIKE, value + Constant.PERCENT);
        return this;
    }

    @Override
    public LambdaQuery<T> nLk(SFunction<T> column, Object value) {
        this.where(FunctionGetter.name(column), SqlKeyword.NOT_LIKE, Constant.PERCENT + value + Constant.PERCENT);
        return this;
    }

    @Override
    public LambdaQuery<T> lt(SFunction<T> column, Object value) {
        this.where(FunctionGetter.name(column), SqlKeyword.LT, value);
        return this;
    }

    @Override
    public LambdaQuery<T> lte(SFunction<T> column, Object value) {
        this.where(FunctionGetter.name(column), SqlKeyword.LTE, value);
        return this;
    }

    @Override
    public LambdaQuery<T> gt(SFunction<T> column, Object value) {
        this.where(FunctionGetter.name(column), SqlKeyword.GT, value);
        return this;
    }

    @Override
    public LambdaQuery<T> gte(SFunction<T> column, Object value) {
        this.where(FunctionGetter.name(column), SqlKeyword.GTE, value);
        return this;
    }

    @Override
    public LambdaQuery<T> in(SFunction<T> column, Collection<?> value) {
        this.where(FunctionGetter.name(column), SqlKeyword.IN, value);
        return this;
    }

    @Override
    public LambdaQuery<T> between(SFunction<T> column, Object valueStart, Object valueEnd) {
        this.whereBetween(FunctionGetter.name(column), valueStart, valueEnd);
        return this;
    }

    @Override
    public LambdaQuery<T> asc(SFunction<T> column) {
        orderByList.add(String.join(Constant.SPACE, FunctionGetter.name(column), SqlKeyword.ASC.get()));
        return this;
    }

    @Override
    public LambdaQuery<T> desc(SFunction<T> column) {
        orderByList.add(String.join(Constant.SPACE, FunctionGetter.name(column), SqlKeyword.DESC.get()));
        return this;
    }

    @Override
    public LambdaQuery<T> groupBy(SFunction<T> column) {
        groupBySet.add(FunctionGetter.name(column));
        return this;
    }

    @SafeVarargs
    @Override
    public final LambdaQuery<T> groupBy(SFunction<T>... columns) {
        groupBySet.addAll(Arrays.stream(columns).map(FunctionGetter::name).collect(Collectors.toSet()));
        return this;
    }

    @Override
    public LambdaQuery<T> having(SFunction<T> column, String opr, Object value) {
        havingSet.add(String.join(Constant.SPACE, FunctionGetter.name(column), opr, String.valueOf(value)));
        return this;
    }

    @Override
    public LambdaQuery<T> tenant(SFunction<T> column, Object tenantId) {
        this.where(FunctionGetter.name(column), SqlKeyword.EQ, tenantId);
        return this;
    }

    @Override
    public LambdaQuery<T> deleted() {
        this.where("deleted", SqlKeyword.EQ, 0);
        return this;
    }
}
