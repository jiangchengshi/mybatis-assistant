package cool.doudou.mybatis.assistant.generator.entity;

/**
 * 类字段
 *
 * @author jiangcs
 * @since 2022/4/20
 */
public class ClassField {
    private String name;
    private String javaType;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
