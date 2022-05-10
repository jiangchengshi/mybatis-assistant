package cool.doudou.mybatis.assistant.expansion.util;

/**
 * ComUtil
 *
 * @author jiangcs
 * @since 2022/4/19
 */
public class ComUtil {
    /**
     * 下划线 转 驼峰
     *
     * @param str 下划线字符串
     * @return 驼峰字符串
     */
    public static String underline2Hump(String str) {
        StringBuilder sbHump = new StringBuilder();
        String[] strArr = str.toLowerCase().split("_");
        for (int i = 0, len = strArr.length; i < len; i++) {
            if (i == 0) {
                sbHump.append(strArr[i]);
            } else {
                sbHump.append(upperFirst(strArr[i]));
            }
        }
        return sbHump.toString();
    }

    /**
     * 首字母大写
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String upperFirst(String str) {
        return str.toUpperCase().charAt(0) + str.substring(1);
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

    /**
     * 数据库数据类型 转换 Java数据类型
     *
     * @param dataType 数据库数据类型
     * @return 字符串
     */
    public static String convert2JavaType(String dataType) {
        switch (dataType) {
            case "binary":
                return "Byte[]";
            case "bit":
                return "Boolean";
            case "char":
            case "varchar":
            case "text":
                return "String";
            case "smallint":
            case "tinyint":
            case "int":
                return "Integer";
            case "float":
                return "Float";
            case "double":
                return "Double";
            case "decimal":
            case "numeric":
                return "BigDecimal";
            case "bigint":
                return "Long";
            case "date":
                return "LocalDate";
            case "time":
                return "LocalTime";
            case "datetime":
            case "timestamp":
                return "LocalDateTime";
            case "blob":
                return "Blob";
            default:
                System.err.println("dataType[" + dataType + "] match fail");
        }
        return null;
    }
}
