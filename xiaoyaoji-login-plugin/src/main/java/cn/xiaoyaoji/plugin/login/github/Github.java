package cn.xiaoyaoji.plugin.login.github;

import cn.xiaoyaoji.plugin.login.AccessToken;
import cn.xiaoyaoji.service.AppCts;
import cn.xiaoyaoji.service.common.HashMapX;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import jodd.http.HttpRequest;

import java.util.List;

/**
 * @author: zhoujingjie
 * @Date: 16/9/2
 */
public class Github {


    public AccessToken getAccessToken(String client_id, String client_secret, String code, String redirect_uri) {
        String url = "https://github.com/login/oauth/access_token";
        String rs = new String(HttpRequest.post(url).form(new HashMapX<String, Object>()
                .append("client_id", client_id)
                .append("client_secret", client_secret)
                .append("code", code)
                .append("redirect_uri", redirect_uri)
        )
                .header("Accept", "application/json")
                .send().bodyBytes(), AppCts.UTF8);

        AccessToken accessToken = JSON.parseObject(rs, AccessToken.class);
        if (accessToken == null || accessToken.getAccess_token() == null) {
            throw new GithubException(rs);
        }
        return accessToken;
    }

    public List<Email> getEmail(String accessToken) {
        String url = "https://api.github.com/user/emails?access_token=" + accessToken;
        String rs = new String(HttpRequest.get(url).send().bodyBytes(), AppCts.UTF8);
        if (rs.contains("message")) {
            throw new GithubException(rs);
        }
        return JSON.parseObject(rs, new TypeReference<List<Email>>() {
        });
    }

    public User getUser(String accessToken) {
        String url = "https://api.github.com/user?access_token=" + accessToken;
        String rs = new String(HttpRequest.get(url).send().bodyBytes(), AppCts.UTF8);
        if (rs.contains("message")) {
            throw new GithubException(rs);
        }
        return JSON.parseObject(rs, User.class);
    }


}
