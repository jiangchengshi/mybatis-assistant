package cool.doudou.mybatis.assistant.core.mapper;

import cool.doudou.mybatis.assistant.core.page.Page;
import cool.doudou.mybatis.assistant.core.query.LambdaQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * BaseMapper
 *
 * @author jiangcs
 * @since 2022/4/4
 */
public interface BaseMapper<T> {
    /**
     * 根据 id 查询记录
     *
     * @param id 主键ID
     * @return 实体记录
     */
    T selectById(Long id);

    /**
     * 查询列表
     *
     * @param page        分页参数
     * @param lambdaQuery 查询参数
     * @return 实体记录集合
     */
    List<T> selectList(@Param("pg") Page page, @Param("q") LambdaQuery<T> lambdaQuery);

    /**
     * 新增
     *
     * @param t 实体参数
     * @return 新增记录数
     */
    int insert(T t);

    /**
     * 更新
     *
     * @param t 实体参数
     * @return 更新记录数
     */
    int update(T t);

    /**
     * 根据 ids 删除
     *
     * @param ids 主键ID，多个以逗号分隔
     * @return 删除记录数
     */
    int deleteByIds(String ids);
}
