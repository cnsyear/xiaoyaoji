package cn.xiaoyaoji.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 *
 * @author: zhoujingjie
 * Date: 2018/9/27
 */
@Component
@ConfigurationProperties(prefix = "xyj",ignoreUnknownFields = true)
public class XyjProperties {
    /**
     * 版本号
     */
    private String version;
    /**
     * 密码盐值
     */
    private String salt = "api";
    /**
     * 会话过期时间 (秒)
     */
    private int tokenExpires = 604800;
    /**
     * 找回密码地址
     */
    private String findpasswordUrl;

    /**
     * 上传的文件访问地址
     */
    private String fileAccess;

    private Provider provider;

    public class Provider {
        /**
         * 文件上传提供者
         */
        private String upload;

        /**
         * 邮件提供者
         */
        private String email;
        /**
         * 缓存提供者
         */
        private String cache;

        public String getUpload() {
            return upload;
        }

        public void setUpload(String upload) {
            this.upload = upload;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCache() {
            return cache;
        }

        public void setCache(String cache) {
            this.cache = cache;
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getTokenExpires() {
        return tokenExpires;
    }

    public void setTokenExpires(int tokenExpires) {
        this.tokenExpires = tokenExpires;
    }

    public String getFindpasswordUrl() {
        return findpasswordUrl;
    }

    public void setFindpasswordUrl(String findpasswordUrl) {
        this.findpasswordUrl = findpasswordUrl;
    }

    public String getFileAccess() {
        return fileAccess;
    }

    public void setFileAccess(String fileAccess) {
        this.fileAccess = fileAccess;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
