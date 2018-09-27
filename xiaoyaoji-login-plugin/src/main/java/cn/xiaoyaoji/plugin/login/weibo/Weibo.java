package cn.xiaoyaoji.plugin.login.weibo;

import cn.xiaoyaoji.service.AppCts;
import cn.xiaoyaoji.service.common.HashMapX;
import com.alibaba.fastjson.JSON;
import jodd.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhoujingjie
 * @date 2016-07-28
 */
public class Weibo {
    private Logger logger = LoggerFactory.getLogger("thirdly");

    /**
     * 获取用户信息
     *
     * @param accessToken
     * @param uid
     * @return
     */
    public User showUser(String accessToken, String uid) {
        String rs = new String(HttpRequest.get("https://api.weibo.com/2/users/show.json?access_token=" + accessToken + "&uid=" + uid).send().bodyBytes(), AppCts.UTF8);
        if (rs.contains("error_code")) {
            throw new WeiboException(rs);
        }
        return JSON.parseObject(rs, User.class);
    }

    /**
     * 获取accessToken
     *
     * @param appKey
     * @param appSecret
     * @param code
     * @param redirectUri
     * @return
     */
    public AccessToken getAccessToken(String appKey, String appSecret, String code, String redirectUri) {

        String rs = new String(HttpRequest.post("https://api.weibo.com/oauth2/access_token")
                .form(new HashMapX<String, Object>()
                        .append("client_id", appKey)
                        .append("client_secret", appSecret)
                        .append("grant_type", "authorization_code")
                        .append("code", code)
                        .append("redirect_uri", redirectUri)
                ).send().bodyBytes(), AppCts.UTF8);
        AccessToken accessToken = JSON.parseObject(rs, AccessToken.class);
        if (accessToken == null || accessToken.getAccess_token() == null) {
            throw new WeiboException(rs);
        }
        return accessToken;
    }


    public String getEmail(String accessToken) {
        String url = "https://api.weibo.com/2/account/profile/email.json?access_token=" + accessToken;
        String rs = HttpRequest.get(url).send().bodyText();
        if (rs.contains("error"))
            throw new WeiboException(rs);
        System.out.println(rs);
        return null;
    }
}
