package cool.doudou.mybatis.assistant.boot.starter.auto.configuration;

import cool.doudou.mybatis.assistant.boot.starter.config.MybatisAssistantConfig;
import cool.doudou.mybatis.assistant.boot.starter.properties.MybatisAssistantProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * MybatisAssistantAutoConfiguration
 *
 * @author jiangcs
 * @since 2022/3/31
 */
@EnableConfigurationProperties(MybatisAssistantProperties.class)
@Import({DataSourceAutoConfiguration.class, MybatisAssistantConfig.class})
@AutoConfiguration
public class MybatisAssistantAutoConfiguration {
}
