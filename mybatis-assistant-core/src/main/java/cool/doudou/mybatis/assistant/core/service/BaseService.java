package cool.doudou.mybatis.assistant.core.service;

import cool.doudou.mybatis.assistant.core.mapper.BaseMapper;
import cool.doudou.mybatis.assistant.core.page.Page;
import cool.doudou.mybatis.assistant.core.page.PageInfo;
import cool.doudou.mybatis.assistant.core.query.LambdaQuery;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * BaseService
 *
 * @author jiangcs
 * @since 2022/5/6
 */
public class BaseService<M extends BaseMapper<T>, T> extends SuperService {
    @Autowired
    private M baseMapper;

    /**
     * 根据 id 查询记录
     *
     * @param id 记录Id
     * @return T
     */
    public T getById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 根据 id 查询记录
     *
     * @param t 实体参数
     * @return T
     */
    public T get(T t) {
        LambdaQuery<T> lambdaQuery = new LambdaQuery<>(t);
        return baseMapper.selectOne(lambdaQuery);
    }

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param t    实体参数
     * @return PageInfo
     */
    public PageInfo<T> page(Page page, T t) {
        LambdaQuery<T> lambdaQuery = new LambdaQuery<>(t);
        return PageInfo.of(baseMapper.selectList(page, lambdaQuery));
    }

    /**
     * 根据 ids 删除记录
     *
     * @param ids 记录Ids
     * @return int
     */
    public boolean delete(String ids) {
        return retBoolean(baseMapper.deleteByIds(ids));
    }

    public M baseMapper() {
        return this.baseMapper;
    }
}
