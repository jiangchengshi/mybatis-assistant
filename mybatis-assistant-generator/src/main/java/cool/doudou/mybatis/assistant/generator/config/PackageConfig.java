package cool.doudou.mybatis.assistant.generator.config;

/**
 * PackageConfig
 *
 * @author jiangcs
 * @since 2022/4/19
 */
public class PackageConfig {
    /**
     * 父级包名
     */
    public String parent = "cool.doudou";
    /**
     * 模块名，默认为""
     */
    private String module = "";
    /**
     * controller包名
     */
    private final String controller = "controller";
    /**
     * service包名
     */
    private final String service = "service";
    /**
     * mapper包名
     */
    private final String mapper = "mapper";
    /**
     * entity包名
     */
    private final String entity = "entity";

    public String getParent() {
        return parent;
    }

    public String getModule() {
        return module;
    }

    public String getController() {
        return controller;
    }

    public String getService() {
        return service;
    }

    public String getMapper() {
        return mapper;
    }

    public String getEntity() {
        return entity;
    }

    public PackageConfig parent(String parent) {
        this.parent = parent;
        return this;
    }

    public PackageConfig module(String module) {
        this.module = module;
        return this;
    }
}
