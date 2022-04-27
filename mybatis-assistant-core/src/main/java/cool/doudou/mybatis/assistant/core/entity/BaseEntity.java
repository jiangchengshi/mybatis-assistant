package cool.doudou.mybatis.assistant.core.entity;

import cool.doudou.mybatiis.assistant.annotation.FieldFill;
import cool.doudou.mybatiis.assistant.annotation.enums.CommandTypeEnum;

import java.time.LocalDateTime;

/**
 * BaseEntity
 *
 * @author jiangcs
 * @since 2022/4/4
 */
public class BaseEntity {
    private Long id;
    private Long createBy;
    @FieldFill(fill = CommandTypeEnum.INSERT)
    private LocalDateTime createTime;
    private Long updateBy;
    @FieldFill(fill = CommandTypeEnum.UPDATE)
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                '}';
    }
}
