package cn.xiaoyaoji.service.biz.user.bean;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 第三方
 *
 * @author: zhoujingjie
 * @Date: 16/5/2
 */
@Table(name = "thirdparty")
public class UserThirdParty {

    @Id
    private Integer id;
    /**
     * 第三方id
     */
    private String thirdId;
    @Transient
    private String nickName;
    @Transient
    private String logo;

    /**
     * 第三方类型/插件id
     */
    private String type;
    @Transient
    private String email;
    /**
     * 用户id
     */
    private String userId;



    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }
}
