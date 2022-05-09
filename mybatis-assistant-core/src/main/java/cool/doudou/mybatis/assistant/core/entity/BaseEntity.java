package cool.doudou.mybatis.assistant.core.entity;

import java.time.LocalDateTime;

/**
 * BaseEntity
 *
 * @author jiangcs
 * @since 2022/4/4
 */
public class BaseEntity {
    /**
     * 记录ID
     */
    private Long id;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    private Long updateBy;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 逻辑删除：0 正常，-1 删除
     */
    private Integer deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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
