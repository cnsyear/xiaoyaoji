package cn.xiaoyaoji.service.plugin;


import java.util.List;
import java.util.Map;

/**
 * 插件信息
 *
 * @author zhoujingjie
 *         created on 2017/5/18
 */
public class PluginInfo {
    /**
     * 插件id
     */
    private String id;
    /**
     * 插件名称
     */
    private String name;
    /**
     * 插件描述
     */
    private String description;
    /**
     * 作者
     */
    private String author;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 需要扫描的类
     */
    private List<String> classes;
    /**
     * 插件版本
     */
    private String version;
    private String event;
    /**
     * 图标
     */
    private Icon icon;
    /**
     * 依赖的系统版本
     */
    private Dependency dependency;
    /**
     * 配置信息
     */
    private Map<String, String> config;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}

