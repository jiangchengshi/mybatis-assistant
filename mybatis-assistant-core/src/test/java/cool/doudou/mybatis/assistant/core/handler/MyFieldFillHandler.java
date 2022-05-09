package cool.doudou.mybatis.assistant.core.handler;

import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * MyFieldFillHandler
 *
 * @author jiangcs
 * @since 2022/4/5
 */
public class MyFieldFillHandler implements IFieldFillHandler {
    @Override
    public void insert(MetaObject metaObject) {
        this.fill(metaObject, "createBy", 1L);
        this.fill(metaObject, "createTime", LocalDateTime.now());
    }

    @Override
    public void update(MetaObject metaObject) {
        this.fill(metaObject, "updateBy", 1L);
        this.fill(metaObject, "updateTime", LocalDateTime.now());
    }
}
