package cn.xiaoyaoji.api.config;

import cn.xiaoyaoji.api.base.SessionTimeoutException;
import cn.xiaoyaoji.service.annotations.Ignore;
import cn.xiaoyaoji.service.common.AppCts;
import cn.xiaoyaoji.service.integration.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 * 　　┃　　　┃神兽保佑
 * 　　┃　　　┃代码无BUG！
 * 　　┃　　　┗━━━┓
 * 　　┃　　　　　　　┣┓
 * 　　┃　　　　　　　┏┛
 * 　　┗┓┓┏━┳┓┏┛
 * 　　　┃┫┫　┃┫┫
 * 　　　┗┻┛　┗┻┛
 * <p>
 *
 * @author zhoujingjie
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private CacheService cacheService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //如果是管理端接口则需要登陆
        String token = request.getHeader(AppCts.TOKEN_NAME);
        if (token == null || cacheService.getUser(token) == null) {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Ignore ignore = handlerMethod.getMethod().getAnnotation(Ignore.class);
                if (ignore == null) {
                    ignore = handlerMethod.getBeanType().getAnnotation(Ignore.class);
                }
                if (ignore != null && ignore.value()) {
                    return true;
                }
            }
            throw new SessionTimeoutException();
        }
        return true;
    }

}
