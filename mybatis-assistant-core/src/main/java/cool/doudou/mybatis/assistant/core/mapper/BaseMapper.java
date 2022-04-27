package cool.doudou.mybatis.assistant.core.mapper;

import cool.doudou.mybatis.assistant.core.page.Page;
import cool.doudou.mybatis.assistant.core.page.PageInfo;
import org.apache.ibatis.annotations.Param;

/**
 * BaseMapper
 *
 * @author jiangcs
 * @since 2022/4/4
 */
public interface BaseMapper<Entity> {
    /**
     * 分页查询
     *
     * @param page   分页参数
     * @param entity 实体参数
     * @return PageInfo
     */
    PageInfo<Entity> selectPage(@Param("pg") Page page, @Param("ett") Entity entity);

    /**
     * 根据 id 查询记录
     *
     * @param id 主键ID
     * @return Entity
     */
    Entity selectById(Long id);

    /**
     * 新增
     *
     * @param entity 实体参数
     * @return 新增记录数
     */
    int insert(Entity entity);

    /**
     * 更新
     *
     * @param entity 实体参数
     * @return 更新记录数
     */
    int update(Entity entity);

    /**
     * 根据 ids 删除
     *
     * @param ids 主键ID，多个以逗号分隔
     * @return 删除记录数
     */
    int deleteByIds(String ids);
}
