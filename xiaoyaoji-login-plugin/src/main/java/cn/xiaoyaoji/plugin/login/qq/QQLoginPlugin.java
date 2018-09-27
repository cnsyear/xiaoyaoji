package cn.xiaoyaoji.plugin.login.qq;


import cn.xiaoyaoji.service.plugin.LoginPlugin;
import cn.xiaoyaoji.service.plugin.LoginThirdParty;
import cn.xiaoyaoji.service.plugin.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zhoujingjie
 *         created on 2017/7/24
 */
public class QQLoginPlugin extends LoginPlugin {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private QQ qq;


    @PostConstruct
    public void init() {
        Map<String, String> config = getPluginInfo().getConfig();
        qq = new QQ(config.get("clientId"), config.get("secret"));
    }

    /**
     * 登录请求操作
     *
     * @param request req
     * @return LoginThirdParty
     */
    @Override
    public LoginThirdParty login(HttpServletRequest request) {
        String openId = request.getParameter("openId");
        String accessToken = request.getParameter("accessToken");
        UserInfo userInfo = qq.getUserInfo(openId, accessToken);
        return new LoginThirdParty(openId, userInfo.getFigureurl_2(), userInfo.getNickname());
    }

    /**
     * 弹窗授权地址。
     */
    @Override
    public String authUrl() {
        String clientId = getPluginInfo().getConfig().get("clientId");
        String redirectUri = getPluginInfo().getConfig().get("redirectUri");
        return "https://graph.qq.com/oauth2.0/authorize?response_type=code&state=login&client_id=" + clientId + "&redirect_uri=" + redirectUri;
    }

    /**
     * 授权成功
     *
     * @param request req
     * @return LoginThirdParty
     */
    @Override
    public LoginThirdParty authSuccess(HttpServletRequest request) {
        String state = request.getParameter("state");
        String code = request.getParameter("code");
        logger.info("callback qq -> code:" + code + " state:" + state);
        if ("login".equals(state)) {
            String redirectUri = getPluginInfo().getConfig().get("redirectUri");
            AccessToken accessToken = qq.getAccessToken(code, redirectUri);
            String openId = qq.getOpenid(accessToken.getAccess_token());
            return new LoginThirdParty(openId, accessToken.getAccess_token());
        }
        throw new PluginException("无效的state:" + state);
    }
}
