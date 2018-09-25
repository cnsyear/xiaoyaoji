package cn.xiaoyaoji.service.biz.doc.bean;

import cn.xiaoyaoji.service.biz.doc.view.DocType;
import cn.xiaoyaoji.service.util.JsonUtils;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 内容
 *
 * @author: zhoujingjie
 * @Date: 17/4/1
 */
@Table(name = "doc")
public class Doc {
    /**
     * 文档id
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 文档类型。 @see {@link DocType}
     */
    private String type;
    /**
     * 文档内容。
     * 如果文档是文件夹、快捷方式、模块 内容为空
     * 如果文档是富文本、markdown 内容是其对应内容
     * 如果文档是http、websocket 则参考对应bean
     * 如果是第三方,则内容是url地址
     *
     * @see cn.xiaoyaoji.service.biz.doc.view.HttpDoc
     * @see cn.xiaoyaoji.service.biz.doc.view.WebsocketDoc
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;
    /**
     * 项目id
     */
    private String projectId;
    //
    private String parentId;
    @Transient
    private List<Doc> children = Collections.emptyList();

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<Doc> getChildren() {
        return children;
    }

    public void setChildren(List<Doc> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return JsonUtils.toString(this);
    }

}
