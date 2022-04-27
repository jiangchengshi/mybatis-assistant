package cool.doudou.mybatis.assistant.core.enums;

/**
 * SqlKeyword
 *
 * @author jiangcs
 * @since 2022/4/21
 */
public enum SqlKeyword {
    AND("AND"),
    OR("OR"),
    IN("IN"),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE"),
    EQ("="),
    NOT_EQ("<>"),
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    EXISTS("EXISTS"),
    BETWEEN("BETWEEN"),
    WHERE("WHERE"),
    GROUP_BY("ORDER BY"),
    HAVING("HAVING"),
    ORDER_BY("ORDER BY"),
    ASC("ASC"),
    DESC("DESC");

    private final String keyword;

    public String get() {
        return keyword;
    }

    SqlKeyword(String keyword) {
        this.keyword = keyword;
    }
}
