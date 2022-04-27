package cool.doudou.mybatis.assistant.boot.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * MybatisAssistantProperties
 *
 * @author jiangcs
 * @since 2022/4/6
 */
@ConfigurationProperties(prefix = "mybatis.assistant")
public class MybatisAssistantProperties {
    private String mapperLocations = "classpath*:mapper/*Mapper.xml";
    private List<String> interceptors = new ArrayList<>();

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public List<String> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<String> interceptors) {
        this.interceptors = interceptors;
    }
}
