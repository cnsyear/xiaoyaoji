package cn.xiaoyaoji.service.biz.project.bean;

import cn.xiaoyaoji.service.util.JsonUtils;

import javax.persistence.Table;

/**
 * @author: zhoujingjie
 * @Date: 17/4/25
 */
@Table(name = "project_global")
public class ProjectGlobal {
    private String id;
    /**
     * 全局环境变量
     */
    private String environment;
    /**
     * 全局http 参数
     */
    private String http;
    /**
     * 全局状态; 即将删除
     */
    @Deprecated
    private String status;
    private String projectId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return JsonUtils.toString(this);
    }
}
