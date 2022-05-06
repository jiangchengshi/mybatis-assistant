package cool.doudou.mybatis.assistant.core.service;

import cool.doudou.mybatis.assistant.core.mapper.BaseMapper;
import cool.doudou.mybatis.assistant.core.page.PageDTO;
import cool.doudou.mybatis.assistant.core.page.PageInfo;
import cool.doudou.mybatis.assistant.core.query.LambdaQuery;

/**
 * BaseService
 *
 * @author jiangcs
 * @since 2022/5/6
 */
public class BaseService<M extends BaseMapper<T>, T> {
    private final M mapper;

    public BaseService(M mapper) {
        this.mapper = mapper;
    }

    /**
     * 分页查询
     *
     * @param pageDTO
     * @return PageInfo<T>
     */
    public PageInfo<T> page(PageDTO<T> pageDTO) {
        LambdaQuery<T> lambdaQuery = new LambdaQuery<>();
        return PageInfo.of(mapper.selectList(pageDTO.page(), lambdaQuery));
    }

    /**
     * 根据 id 查询记录
     *
     * @param id
     * @return T
     */
    public T get(Long id) {
        return mapper.selectById(id);
    }

    /**
     * 添加记录
     *
     * @param t
     * @return int
     */
    public int add(T t) {
        return mapper.insert(t);
    }

    /**
     * 修改记录
     *
     * @param t
     * @return int
     */
    public int modify(T t) {
        return mapper.update(t);
    }

    /**
     * 根据 ids 删除记录
     *
     * @param ids
     * @return int
     */
    public int delete(String ids) {
        return mapper.deleteByIds(ids);
    }
}
