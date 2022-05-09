package cool.doudou.mybatis.assistant.core.handler;

/**
 * MyTenantFillHandler
 *
 * @author jiangcs
 * @since 2022/5/9
 */
public class MyTenantFillHandler implements ITenantFillHandler {
    @Override
    public Long getTenantId() {
        return 1L;
    }
}
