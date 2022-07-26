package cool.doudou.mybatis.assistant.boot.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MybatisAssistantProperties
 *
 * @author jiangcs
 * @since 2022/4/6
 */
@ConfigurationProperties(prefix = "mybatis.assistant")
public class MybatisAssistantProperties {
    private String mapperLocations = "classpath*:mapper/*Mapper.xml";
    private boolean logEnable = false;

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public boolean isLogEnable() {
        return logEnable;
    }

    public void setLogEnable(boolean logEnable) {
        this.logEnable = logEnable;
    }
}
