package cool.doudou.mybatis.assistant.core.common;

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

    /**
     * 查询参数
     */
    public interface QUERY {
        String paramName = "q";
        String paramPrefix = "param";
    }

    /**
     * 拦截器名称
     */
    public interface InterceptorName {
        String QUERY = "query";
        String FILL = "fill";
        String DESENSITIZE = "desensitize";
    }
}
