package cool.doudou.mybatis.assistant.generator.entity;

/**
 * 列信息
 *
 * @author jiangcs
 * @since 2022/4/18
 */
public class Column {
    /**
     * 字段名：COLUMN_NAME
     */
    private String name;
    /**
     * 数据类型：DATA_TYPE
     */
    private String dataType;
    /**
     * 注释：COLUMN_COMMENT
     */
    private String comment;
    /**
     * 关键字：COLUMN_KEY（PRI、UNI、MUL）
     */
    private String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
