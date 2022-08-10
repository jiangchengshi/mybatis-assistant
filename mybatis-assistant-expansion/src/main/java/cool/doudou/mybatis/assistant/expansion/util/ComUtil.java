package cool.doudou.mybatis.assistant.expansion.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ComUtil
 *
 * @author jiangcs
 * @since 2022/4/19
 */
public class ComUtil {
    private static final Pattern linePattern = Pattern.compile("_(\\w)");
    private static final Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 下划线 转 驼峰
     *
     * @param str 下划线字符串
     * @return 驼峰字符串
     */
    public static String underline2Hump(String str) {
        Matcher matcher = linePattern.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰 转 下划线
     *
     * @param str 驼峰字符串
     * @return 下划线字符串
     */
    public static String hump2Underline(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 首字母小写
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String lowerFirst(String str) {
        return str.toLowerCase().charAt(0) + str.substring(1);
    }
}
