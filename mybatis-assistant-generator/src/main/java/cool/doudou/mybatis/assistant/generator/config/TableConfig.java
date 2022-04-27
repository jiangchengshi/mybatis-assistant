package cool.doudou.mybatis.assistant.generator.config;

import java.util.List;

/**
 * 数据表 配置
 *
 * @author jiangcs
 * @since 2022/4/19
 */
public class TableConfig {
    /**
     * 数据表Schema
     */
    private String schema;
    /**
     * 数据表名集合
     */
    private List<String> nameList;

    public TableConfig schema(String schema) {
        this.schema = schema;
        return this;
    }

    public TableConfig nameList(String... names) {
        return this.nameList(List.of(names));
    }

    public TableConfig nameList(List<String> nameList) {
        this.nameList = nameList;
        return this;
    }

    public String getSchema() {
        return schema;
    }

    public List<String> getNameList() {
        return nameList;
    }
}
