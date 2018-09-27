package cn.xiaoyaoji.plugin.login.github;

import cn.xiaoyaoji.service.plugin.LoginPlugin;
import cn.xiaoyaoji.service.plugin.LoginThirdParty;
import cn.xiaoyaoji.service.plugin.PluginException;
import cn.xiaoyaoji.service.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhoujingjie
 *         created on 2017/7/24
 */
public class GithubLoginPlugin extends LoginPlugin {
    private Logger logger = LoggerFactory.getLogger(GithubLoginPlugin.class);

    @Override
    public LoginThirdParty login(HttpServletRequest request) {
        String accessToken = request.getParameter("accessToken");
        AssertUtils.notNull(accessToken, "missing accessToken");
        Github github = new Github();
        cn.xiaoyaoji.plugin.login.github.User user = github.getUser(accessToken);
        return new LoginThirdParty(user.getId(), user.getAvatar_url(), user.getName());
    }

    /**
     * 授权地址
     *
     * @return
     */
    @Override
    public String authUrl() {
        String clientid = getPluginInfo().getConfig().get("clientId");
        String redirectUri = getPluginInfo().getConfig().get("redirectUri");
        return "https://github.com/login/oauth/authorize?client_id=" + clientid + "&redirect_uri=" + redirectUri + "&scope=user&state=login";
    }

    /**
     * 授权成功
     *
     * @param request req
     * @return
     */
    @Override
    public LoginThirdParty authSuccess(HttpServletRequest request) {
        String state = request.getParameter("state");
        String code = request.getParameter("code");
        logger.info("callback github -> code:" + code + " state:" + state);
        if ("login".contains(state)) {
            Github github = new Github();
            String clientId = getPluginInfo().getConfig().get("clientId");
            String secret = getPluginInfo().getConfig().get("secret");
            String redirectUri = getPluginInfo().getConfig().get("redirectUri");
            cn.xiaoyaoji.plugin.login.AccessToken accessToken = github.getAccessToken(clientId, secret, code, redirectUri);
            cn.xiaoyaoji.plugin.login.github.User user = github.getUser(accessToken.getAccess_token());
            return new LoginThirdParty(user.getId(), accessToken.getAccess_token());
        }
        throw new PluginException("无效的statue:" + state);
    }
}
