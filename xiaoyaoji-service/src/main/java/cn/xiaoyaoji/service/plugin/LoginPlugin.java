package cn.xiaoyaoji.service.plugin;


import javax.servlet.http.HttpServletRequest;

/**
 * 登录插件
 *
 * @author zhoujingjie
 *         created on 2017/7/24
 */
public abstract class LoginPlugin extends Plugin {

    /**
     * 登录请求操作
     *
     * @param request req
     * @return LoginThirdParty
     */
    public abstract LoginThirdParty login(HttpServletRequest request);


    /**
     * 弹窗授权地址。
     */
    public abstract String authUrl();


    /**
     * 授权成功
     *
     * @param request req
     * @return LoginThirdParty
     */
    public abstract LoginThirdParty authSuccess(HttpServletRequest request);

}
