package cn.xiaoyaoji.api.config;

import cn.xiaoyaoji.api.base.Session;
import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.service.AppCts;
import cn.xiaoyaoji.service.spi.CacheService;
import com.google.common.base.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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
 * <p>
 * 处理含有@Session注解的参数
 *
 * @author: zhoujingjie
 * Date: 2018/7/31
 */
public class SessionHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private CacheService cacheService;

    public SessionHandlerMethodArgumentResolver(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Session.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        Session session = parameter.getParameterAnnotation(Session.class);
        String key = session.key();
        if (Strings.isNullOrEmpty(key)) {
            key = session.value();
        }
        if (Strings.isNullOrEmpty(key)) {
            if (parameter.getParameterType().isAssignableFrom(User.class)) {
                key = AppCts.TOKEN_NAME;
            } else {
                key = parameter.getParameterName();
            }
        }
        String keyValue = webRequest.getHeader(key);
        if (AppCts.TOKEN_NAME.equals(key)) {
            keyValue = key + ":" + keyValue;
        }
        if(keyValue == null){
            return null;
        }
        return cacheService.get(keyValue, parameter.getParameterType());
    }
}
