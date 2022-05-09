package cool.doudou.mybatis.assistant.core;

import cool.doudou.mybatis.assistant.core.entity.User;
import cool.doudou.mybatis.assistant.core.handler.MyDeletedFillHandler;
import cool.doudou.mybatis.assistant.core.handler.MyFieldFillHandler;
import cool.doudou.mybatis.assistant.core.handler.MyTenantFillHandler;
import cool.doudou.mybatis.assistant.core.helper.MybatisConfigHelper;
import cool.doudou.mybatis.assistant.core.interceptors.FillInterceptor;
import cool.doudou.mybatis.assistant.core.interceptors.QueryInterceptor;
import cool.doudou.mybatis.assistant.core.mapper.UserMapper;
import cool.doudou.mybatis.assistant.core.page.PageDTO;
import cool.doudou.mybatis.assistant.core.query.LambdaQuery;
import cool.doudou.mybatis.assistant.core.query.Query;
import cool.doudou.mybatis.assistant.expansion.dialect.DialectHandlerFactory;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * MybatisAssistantCoreTest
 *
 * @author jiangcs
 * @since 2022/3/31
 */
public class MybatisAssistantCoreTest {
    @Test
    public void testQuery() {
        SqlSession sqlSession = null;

        try {
            Properties properties = new Properties();
            properties.put("dialectHandler", DialectHandlerFactory.getInstance("jdbc:mysql"));
            MybatisConfigHelper.getConfiguration().getInterceptors().forEach((interceptor -> {
                if (interceptor instanceof QueryInterceptor) {
                    interceptor.setProperties(properties);
                }
            }));

            sqlSession = MybatisConfigHelper.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            Query query = new Query();
            query.lk("py", "C")
                    .gt("id", 1);
            query.desc("id");
            List<User> userList = userMapper.selectList(query);
            Assertions.assertTrue(userList != null && userList.size() > 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert sqlSession != null;
            sqlSession.close();
        }
    }

    @Test
    public void testPage() {
        SqlSession sqlSession = null;

        try {
            Properties properties = new Properties();
            properties.put("dialectHandler", DialectHandlerFactory.getInstance("jdbc:mysql"));
            MybatisConfigHelper.getConfiguration().getInterceptors().forEach((interceptor -> {
                if (interceptor instanceof QueryInterceptor) {
                    interceptor.setProperties(properties);
                }
            }));

            sqlSession = MybatisConfigHelper.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            PageDTO<User> pageDTO = new PageDTO<>(2, 20);

            LambdaQuery<User> lambdaQuery = new LambdaQuery<>();
            lambdaQuery.lk(User::getPy, "C");
            lambdaQuery.asc(User::getId);
            List<User> userList = userMapper.selectPage(pageDTO.page(), lambdaQuery);
            Assertions.assertTrue(userList != null && userList.size() > 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert sqlSession != null;
            sqlSession.close();
        }
    }

    @Test
    public void testInsert() {
        SqlSession sqlSession = null;

        try {
            Properties properties = new Properties();
            properties.put("fieldFillHandler", new MyFieldFillHandler());
            MybatisConfigHelper.getConfiguration().getInterceptors().forEach((interceptor -> {
                if (interceptor instanceof FillInterceptor) {
                    interceptor.setProperties(properties);
                }
            }));

            sqlSession = MybatisConfigHelper.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            User user = new User();
            user.setName("testInsert2");
            user.setPy("ti2");
            int count = userMapper.insert(user);
            Assertions.assertTrue(count > 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert sqlSession != null;
            sqlSession.close();
        }
    }

    @Test
    public void testBatchInsert() {
        SqlSession sqlSession = null;

        try {
            Properties properties = new Properties();
            properties.put("fieldFillHandler", new MyFieldFillHandler());
            MybatisConfigHelper.getConfiguration().getInterceptors().forEach((interceptor -> {
                if (interceptor instanceof FillInterceptor) {
                    interceptor.setProperties(properties);
                }
            }));

            sqlSession = MybatisConfigHelper.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            List<User> userList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                User user = new User();
                user.setName("batchInsert" + i);
                user.setPy("bi" + i);
                userList.add(user);
            }
            int count = userMapper.batchInsert(userList);
            Assertions.assertTrue(count > 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert sqlSession != null;
            sqlSession.close();
        }
    }

    @Test
    public void testTenant() {
        SqlSession sqlSession = null;

        try {
            Properties properties = new Properties();
            properties.put("tenantFillHandler", new MyTenantFillHandler());
            MybatisConfigHelper.getConfiguration().getInterceptors().forEach((interceptor -> {
                if (interceptor instanceof FillInterceptor) {
                    interceptor.setProperties(properties);
                }
            }));

            sqlSession = MybatisConfigHelper.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            User user = new User();
            user.setName("testTenant");
            user.setPy("tt");
            int count = userMapper.insert(user);
            Assertions.assertTrue(count > 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert sqlSession != null;
            sqlSession.close();
        }
    }

    @Test
    public void testDeleted() {
        SqlSession sqlSession = null;

        try {
            Properties properties = new Properties();
            properties.put("deletedFillHandler", new MyDeletedFillHandler());
            MybatisConfigHelper.getConfiguration().getInterceptors().forEach((interceptor -> {
                if (interceptor instanceof FillInterceptor) {
                    interceptor.setProperties(properties);
                }
            }));

            sqlSession = MybatisConfigHelper.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            User user = new User();
            user.setName("testDeleted");
            user.setPy("td");
            int count = userMapper.insert(user);
            Assertions.assertTrue(count > 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert sqlSession != null;
            sqlSession.close();
        }
    }
}