package cool.doudou.mybatis.assistant.core;

/**
 * Constant
 *
 * @author jiangcs
 * @since 2022/4/21
 */
public class Constant {
    public static final String SPACE = " ";
    public static final String DOT = ".";
    public static final String COMMA = ",";
    public static final String PERCENT = "%";
    public static final String BRACKETS_LEFT = "(";
    public static final String BRACKETS_RIGHT = ")";
    public static final String PARAM_BRACES_LEFT = "#{";
    public static final String PARAM_BRACES_RIGHT = "}";

    public static interface QUERY {
        String paramName = "q";
        String paramPrefix = "param";
    }
}
