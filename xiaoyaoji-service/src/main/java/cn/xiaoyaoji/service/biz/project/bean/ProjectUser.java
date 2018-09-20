package cn.xiaoyaoji.service.biz.project.bean;

import javax.persistence.Table;
import java.util.Date;

/**
 * @author zhoujingjie
 * @date 2016-07-20
 */
@Table(name = "project_user")
public class ProjectUser {
    private String id;
    private String projectId;
    private String userId;
    private Date createTime;
    /**
     * 状态;1:待接受,2:已接受;3:已拒绝
     */
    private Integer status;
    /**
     * 是否可编辑;1:是;0:否
     */
    private Integer editable;
    /**
     * 使用是常用;1:是;0:否
     */
    private Integer commonlyUsed;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getEditable() {
        return editable;
    }

    public void setEditable(Integer editable) {
        this.editable = editable;
    }

    public Integer getCommonlyUsed() {
        return commonlyUsed;
    }

    public void setCommonlyUsed(Integer commonlyUsed) {
        this.commonlyUsed = commonlyUsed;
    }
}
