package cn.xiaoyaoji.api.spii;

import cn.xiaoyaoji.service.XyjProperties;
import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.service.spi.CacheService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

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
 * 默认实现类
 *
 * @author: zhoujingjie
 * Date: 2018/9/19
 */
public class DefaultCacheService implements CacheService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private XyjProperties xyjProperties;

    private Cache<String, Object> cache;

    @PostConstruct
    private void init() {
        cache = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(xyjProperties.getTokenExpires(), TimeUnit.SECONDS).build();
    }

    @Override
    public User getUser(String token) {
        return (User) cache.getIfPresent("user:" + token);

    }

    @Override
    public void cacheUser(String token, User user) {
        cache.put("user:"+token,user);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return (T) cache.getIfPresent(key);
    }

    @Override
    public void set(String key, Object value) {
        cache.put(key, value);
    }

    /**
     * 删除key
     *
     * @param key
     */
    @Override
    public void remove(String key) {
        cache.invalidate(key);
    }
}
