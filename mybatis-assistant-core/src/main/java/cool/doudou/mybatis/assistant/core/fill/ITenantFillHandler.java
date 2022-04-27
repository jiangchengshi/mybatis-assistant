package cool.doudou.mybatis.assistant.core.fill;

/**
 * ITenantFillHandler
 *
 * @author jiangcs
 * @since 2022/4/9
 */
public interface ITenantFillHandler {
    /**
     * 自定义获取租户ID
     *
     * @return 租户ID表达式
     */
    Long getTenantId();

    /**
     * 忽略 解析、拼接 租户字段
     *
     * @param tableName 数据表名
     * @return false：需要解析、拼接 租户字段
     */
    default boolean ignore(String tableName) {
        return false;
    }
}
