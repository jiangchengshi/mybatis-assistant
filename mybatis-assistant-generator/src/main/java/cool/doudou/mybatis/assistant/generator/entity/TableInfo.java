package cool.doudou.mybatis.assistant.generator.entity;

import java.util.List;

/**
 * 表信息
 *
 * @author jiangcs
 * @since 2022/4/18
 */
public class TableInfo {
    /**
     * 数据表名：
     */
    private String name;
    /**
     * 注释：TABLE_COMMENT
     */
    private String comment;
    /**
     * 列集合
     */
    private List<Column> columnList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }
}
