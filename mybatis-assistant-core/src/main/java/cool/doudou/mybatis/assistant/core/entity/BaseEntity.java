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
    @FieldFill(fill = CommandTypeEnum.INSERT)
    private Long createBy;
    @FieldFill(fill = CommandTypeEnum.INSERT)
    private LocalDateTime createTime;
    @FieldFill(fill = CommandTypeEnum.UPDATE)
    private Long updateBy;
    @FieldFill(fill = CommandTypeEnum.UPDATE)
    private LocalDateTime updateTime;
    private Integer deleted;

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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                '}';
    }
}
