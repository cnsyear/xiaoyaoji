package cn.xiaoyaoji.service.biz.project.bean;

import cn.xiaoyaoji.service.biz.doc.bean.Doc;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 项目
 *
 * @author zhoujingjie
 * @date 2016-07-13
 */
@Table(name = "project")
public class Project {
    private String id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 简单描述
     */
    private String description;
    private String userId;
    @Transient
    private String userName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 状态 ;1:有效;2:无效;3:已删除;4:已存档
     */
    private Integer status;
    /**
     * 权限 1:公开;0:私有
     */
    private Integer permission;
    /**
     * 详细说明
     */
    private String details;
    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;
    //是否可编辑
    @Transient
    private String editable;
    //是否常用项目
    @Transient
    private String commonlyUsed;


    /**
     * 文档列表、导入、导出的时候使用
     */
    @Transient
    private List<Doc> docs = Collections.emptyList();
    /**
     * 全局项目对象。导入、导出的时候使用
     */
    @Transient
    private ProjectGlobal projectGlobal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getCommonlyUsed() {
        return commonlyUsed;
    }

    public void setCommonlyUsed(String commonlyUsed) {
        this.commonlyUsed = commonlyUsed;
    }

    public String getExpires() {
        if (lastUpdateTime == null) {
            return "1天";
        }
        String time = null;
        int day = (int) ((lastUpdateTime.getTime() - System.currentTimeMillis()) / 1000 / 60 / 60 / 24);
        day += 30;
        if (day <= 0) {
            time = "即将";
        } else {
            time = day + "天";
        }
        return time;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<Doc> getDocs() {
        return docs;
    }

    public void setDocs(List<Doc> docs) {
        this.docs = docs;
    }

    public ProjectGlobal getProjectGlobal() {
        return projectGlobal;
    }

    public void setProjectGlobal(ProjectGlobal projectGlobal) {
        this.projectGlobal = projectGlobal;
    }
}
