package cn.xiaoyaoji.service.biz.user.bean;

import cn.xiaoyaoji.service.annotations.Ignore;
import cn.xiaoyaoji.service.util.JsonUtils;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户
 *
 * @author: zhoujingjie
 * @Date: 16/5/22
 */
@Table(name = "user")
public class User implements Serializable {
    private String id;
    private String nickname;
    private Date createtime;
    private String email;
    private String password;
    private String type;
    private String avatar;
    /**
     * 状态;1:有效;0:无效
     */
    private Integer status;

    @Transient
    private String editable;

    public interface Type {
        String USER = "USER";
    }

    @Ignore
    private Map<String, Boolean> bindingMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    @Override
    public String toString() {
        return JsonUtils.toString(this);
    }

    public Map<String, Boolean> getBindingMap() {
        if (bindingMap == null) {
            bindingMap = new HashMap<>();
        }
        return bindingMap;
    }

    public void setBindingMap(Map<String, Boolean> bindingMap) {
        this.bindingMap = bindingMap;
    }
}
