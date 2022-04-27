package cool.doudou.mybatis.assistant.core.handler;

import cool.doudou.mybatis.assistant.core.fill.ITenantFillHandler;

/**
 * MyTenantFillHandler
 *
 * @author jiangcs
 * @since 2022/4/9
 */
public class MyTenantFillHandler implements ITenantFillHandler {
    @Override
    public Long getTenantId() {
        return 0L;
    }
}
