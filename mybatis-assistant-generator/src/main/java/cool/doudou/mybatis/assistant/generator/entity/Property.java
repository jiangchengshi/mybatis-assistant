package cool.doudou.mybatis.assistant.generator.entity;

/**
 * 属性信息
 *
 * @author jiangcs
 * @since 2022/4/18
 */
public class Property {
    /**
     * 属性名
     */
    private String name;
    /**
     * java数据类型
     */
    private String javaType;
    /**
     * 非空
     */
    private boolean notNull;
    /**
     * 注释
     */
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
