package cool.doudou.mybatis.assistant.core.mapper;

import cool.doudou.mybatis.assistant.core.page.Page;
import cool.doudou.mybatis.assistant.core.query.LambdaQuery;
import cool.doudou.mybatis.assistant.core.query.Query;
import cool.doudou.mybatis.assistant.core.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserMapper
 *
 * @author jiangcs
 * @since 2022/3/31
 */
@Mapper
public interface UserMapper {
    List<User> selectList(@Param("q") Query<User> query);

    List<User> selectPage(@Param("pg") Page page, @Param("q") LambdaQuery<User> lambdaQuery);

    int insert(User user);

    int batchInsert(List<User> userList);
}
