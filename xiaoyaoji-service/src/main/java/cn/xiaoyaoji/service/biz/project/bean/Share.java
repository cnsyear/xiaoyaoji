package cn.xiaoyaoji.service.biz.project.bean;

import com.google.common.base.Strings;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author zhoujingjie
 * @date 2016-10-20
 */
@Table(name = "share")
public class Share {
    private String id;
    private String name;
    private String userId;
    @Transient
    private String username;
    private Date createTime;
    private String shareAll;
    @Transient
    private String moduleIds;

    //分享id
    private String docIds;
    private String password;
    private String projectId;
    @Transient
    private String docNames;

    public interface ShareAll {
        String YES = "YES";
        String NO = "NO";
    }

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

    public String getShareAll() {
        return shareAll;
    }

    public void setShareAll(String shareAll) {
        this.shareAll = shareAll;
    }

    public String getModuleIds() {
        return moduleIds;
    }

    public String[] getModuleIdsArray() {
        if (!Strings.isNullOrEmpty(moduleIds)) {
            return moduleIds.split(",");
        }
        return new String[]{};
    }

    public String[] getDocIdsArray() {
        if (!Strings.isNullOrEmpty(docIds)) {
            return docIds.split(",");
        }
        return new String[]{};
    }

    public void setModuleIds(String moduleIds) {
        this.moduleIds = moduleIds;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDocIds() {
        return docIds;
    }

    public void setDocIds(String docIds) {
        this.docIds = docIds;
    }

    public String getDocNames() {
        return docNames;
    }

    public void setDocNames(String docNames) {
        this.docNames = docNames;
    }
}
