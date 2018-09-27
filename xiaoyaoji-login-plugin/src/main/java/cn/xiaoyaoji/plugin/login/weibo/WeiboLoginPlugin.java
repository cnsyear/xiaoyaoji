package cn.xiaoyaoji.plugin.login.weibo;

import cn.xiaoyaoji.service.plugin.LoginPlugin;
import cn.xiaoyaoji.service.plugin.LoginThirdParty;
import cn.xiaoyaoji.service.plugin.PluginException;
import cn.xiaoyaoji.service.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zhoujingjie
 *         created on 2017/7/24
 */
public class WeiboLoginPlugin extends LoginPlugin {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param request req
     * @return LoginThirdParty
     */
    @Override
    public LoginThirdParty login(HttpServletRequest request) {
        String openId = request.getParameter("openId");
        String accessToken = request.getParameter("accessToken");
        AssertUtils.notNull(accessToken, "missing accessToken");
        AssertUtils.notNull(openId, "missing openId");
        AssertUtils.notNull(accessToken, "missing accessToken");
        cn.xiaoyaoji.plugin.login.weibo.User weiboUser = new Weibo().showUser(accessToken, openId);
        return new LoginThirdParty(openId, weiboUser.getAvatar_large(), weiboUser.getScreen_name());
    }

    @Override
    public String authUrl() {
        String clientid = getPluginInfo().getConfig().get("clientId");
        String redirectUri = getPluginInfo().getConfig().get("redirectUri");
        return "https://api.weibo.com/oauth2/authorize?client_id=" + clientid + "&state=login&redirect_uri=" + redirectUri;
    }

    @Override
    public LoginThirdParty authSuccess(HttpServletRequest request) {
        String state = request.getParameter("state");
        String code = request.getParameter("code");
        logger.info("callback weibo -> code:" + code + " state:" + state);
        if ("login".contains(state)) {
            Weibo weibo = new Weibo();
            Map<String, String> config = getPluginInfo().getConfig();
            cn.xiaoyaoji.plugin.login.weibo.AccessToken accessToken = weibo.getAccessToken(config.get("clientId"), config.get("secret"), code, config.get("redirectUri"));
            return new LoginThirdParty(accessToken.getUid(), accessToken.getAccess_token());
        }
        throw new PluginException("无效的state:" + state);
    }
}
