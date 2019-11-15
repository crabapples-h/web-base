package cn.crabapples.entity;



import com.alibaba.fastjson.JSONObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO 实体类基础属性
 *
 * @author Mr.He
 * @date 2019/7/21 15:02
 * e-mail wishforyou.xia@gmail.com
 * pc-name 29404
 */
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 64)
    private String id;
    @Column(columnDefinition = "bit(1) default 0 comment '删除标记 0:正常，1:删除'")
    private int deleteFlag;
    @Column(columnDefinition = "timestamp default CURRENT_TIMESTAMP comment '创建时间'")
    private LocalDateTime createTime;
    @Column(columnDefinition = "timestamp default CURRENT_TIMESTAMP comment '修改时间'")
    private LocalDateTime updateTime;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getDeleteFlag() {
        return deleteFlag;
    }
    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
