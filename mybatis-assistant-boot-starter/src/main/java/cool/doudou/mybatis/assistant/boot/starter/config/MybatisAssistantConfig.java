package cool.doudou.mybatis.assistant.boot.starter.config;

import cool.doudou.mybatis.assistant.boot.starter.interceptor.InterceptorFactory;
import cool.doudou.mybatis.assistant.boot.starter.properties.MybatisAssistantProperties;
import cool.doudou.mybatis.assistant.core.common.Constant;
import cool.doudou.mybatis.assistant.core.handler.*;
import cool.doudou.mybatis.assistant.expansion.dialect.DialectHandlerFactory;
import cool.doudou.mybatis.assistant.expansion.dialect.IDialectHandler;
import org.apache.ibatis.logging.stdout.StdOutImpl;
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
    private IIdFillHandler idFillHandler;
    private IFieldFillHandler fieldFillHandler;
    private IDeletedFillHandler deletedFillHandler;
    private ITenantFillHandler tenantFillHandler;
    private IDesensitizeHandler desensitizeHandler;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        // 设置数据源
        factoryBean.setDataSource(dataSource);

        // 设置SQL映射文件
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resourcePatternResolver.getResources(mybatisAssistantProperties.getMapperLocations()));


        // 返回SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
        if (sqlSessionFactory != null) {
            // 日志
            if (mybatisAssistantProperties.isLogEnable()) {
                sqlSessionFactory.getConfiguration().setLogImpl(StdOutImpl.class);
            }

            // 插件
            addInterceptor(sqlSessionFactory);
        }
        return sqlSessionFactory;
    }

    private void addInterceptor(SqlSessionFactory sqlSessionFactory) {
        // 查询插件
        Interceptor queryInterceptor = InterceptorFactory.getInstance(Constant.InterceptorName.QUERY);
        if (queryInterceptor != null) {
            Properties properties = new Properties();
            IDialectHandler dialectHandler = DialectHandlerFactory.getInstance(jdbcUrl);
            if (dialectHandler != null) {
                properties.put("dialectHandler", dialectHandler);
                queryInterceptor.setProperties(properties);
                sqlSessionFactory.getConfiguration().addInterceptor(queryInterceptor);

                System.out.println("interceptor[" + Constant.InterceptorName.QUERY + "] add ok.");
            }
        }

        // 注入插件
        Interceptor fillInterceptor = InterceptorFactory.getInstance(Constant.InterceptorName.FILL);
        if (fillInterceptor != null) {
            // Id处理器
            if (idFillHandler == null) {
                System.err.println("interceptor[" + Constant.InterceptorName.FILL + "].idFillHandler must be initialized");
            }
            // 字段处理器
            if (fieldFillHandler == null) {
                System.err.println("interceptor[" + Constant.InterceptorName.FILL + "].fieldFillHandler must be initialized");
            }
            // 逻辑处理器
            else if (deletedFillHandler == null) {
                System.err.println("interceptor[" + Constant.InterceptorName.FILL + "].deletedFillHandler must be initialized");
            }
            // 租户处理器
            else if (tenantFillHandler == null) {
                System.err.println("interceptor[" + Constant.InterceptorName.FILL + "].tenantFillHandler must be initialized");
            } else {
                Properties properties = new Properties();
                properties.put("idFillHandler", idFillHandler);
                properties.put("fieldFillHandler", fieldFillHandler);
                properties.put("deletedFillHandler", deletedFillHandler);
                properties.put("tenantFillHandler", tenantFillHandler);
                fillInterceptor.setProperties(properties);
                sqlSessionFactory.getConfiguration().addInterceptor(fillInterceptor);

                System.out.println("interceptor[" + Constant.InterceptorName.FILL + "] add ok.");
            }
        }

        // 脱敏插件
        Interceptor desensitizeInterceptor = InterceptorFactory.getInstance(Constant.InterceptorName.DESENSITIZE);
        if (desensitizeInterceptor != null) {
            if (desensitizeHandler != null) {
                Properties properties = new Properties();
                properties.put("desensitizeHandler", desensitizeHandler);
                desensitizeInterceptor.setProperties(properties);
                sqlSessionFactory.getConfiguration().addInterceptor(desensitizeInterceptor);

                System.out.println("interceptor[" + Constant.InterceptorName.DESENSITIZE + "] add ok.");
            }
        }
    }

    @Autowired
    public void setMybatisAssistantProperties(MybatisAssistantProperties mybatisAssistantProperties) {
        this.mybatisAssistantProperties = mybatisAssistantProperties;
    }

    @Autowired(required = false)
    public void setIdFillHandler(IIdFillHandler idFillHandler) {
        this.idFillHandler = idFillHandler;
    }

    @Autowired(required = false)
    public void setFieldFillHandler(IFieldFillHandler fieldFillHandler) {
        this.fieldFillHandler = fieldFillHandler;
    }

    @Autowired(required = false)
    public void setDeletedFillHandler(IDeletedFillHandler deletedFillHandler) {
        this.deletedFillHandler = deletedFillHandler;
    }

    @Autowired(required = false)
    public void setTenantFillHandler(ITenantFillHandler tenantFillHandler) {
        this.tenantFillHandler = tenantFillHandler;
    }

    @Autowired(required = false)
    public void setDesensitizeHandler(IDesensitizeHandler desensitizeHandler) {
        this.desensitizeHandler = desensitizeHandler;
    }
}
