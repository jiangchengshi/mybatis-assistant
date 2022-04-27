package cool.doudou.mybatis.assistant.boot.starter.config;

import cool.doudou.mybatis.assistant.boot.starter.interceptor.InterceptorFactory;
import cool.doudou.mybatis.assistant.boot.starter.properties.MybatisAssistantProperties;
import cool.doudou.mybatis.assistant.core.dialect.DialectHandlerFactory;
import cool.doudou.mybatis.assistant.core.dialect.IDialectHandler;
import cool.doudou.mybatis.assistant.core.fill.IFieldFillHandler;
import cool.doudou.mybatis.assistant.core.interceptors.FieldFillInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * MybatisAssistantConfig
 *
 * @author jiangcs
 * @since 2022/4/7
 */
public class MybatisAssistantConfig {
    private MybatisAssistantProperties mybatisAssistantProperties;
    private IFieldFillHandler fieldFillHandler;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resourcePatternResolver.getResources(mybatisAssistantProperties.getMapperLocations()));

        // 返回SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();

        // 插件
        addInterceptor(sqlSessionFactory);

        return sqlSessionFactory;
    }

    private void addInterceptor(SqlSessionFactory sqlSessionFactory) {
        // 查询插件
        IDialectHandler dialectHandler = DialectHandlerFactory.getInstance(jdbcUrl);
        if (dialectHandler != null) {
            Interceptor interceptor = InterceptorFactory.getInstance("query");

            Properties properties = new Properties();
            properties.put("dialectHandler", dialectHandler);
            interceptor.setProperties(properties);

            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        }

        // 其他插件：按配置注入
        mybatisAssistantProperties.getInterceptors().forEach(key -> {
            Interceptor interceptor = InterceptorFactory.getInstance(key);

            // 字段填充插件
            if (interceptor instanceof FieldFillInterceptor) {
                if (this.fieldFillHandler != null) {
                    Properties properties = new Properties();
                    properties.put("fieldFillHandler", fieldFillHandler);
                    interceptor.setProperties(properties);

                    sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
                }
            }
        });
    }

    @Autowired
    public void setMybatisAssistantProperties(MybatisAssistantProperties mybatisAssistantProperties) {
        this.mybatisAssistantProperties = mybatisAssistantProperties;
    }

    @Autowired(required = false)
    public void setFieldFillHandler(IFieldFillHandler fieldFillHandler) {
        this.fieldFillHandler = fieldFillHandler;
    }
}
