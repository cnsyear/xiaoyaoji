package cn.xiaoyaoji.api.extension.cache.factory;

import cn.xiaoyaoji.service.util.ConfigUtils;
import cn.xiaoyaoji.service.integration.cache.CacheProvider;
import cn.xiaoyaoji.service.integration.cache.CacheFactory;

/**
 * //todo 该功能做成插件
 *
 * @author zhoujingjie
 *         created on 2017/5/18
 */
public class RedisCacheFactory implements CacheFactory {
    @Override
    public CacheProvider create() {
        return null;
       /* return new RedisCacheProvider(ConfigUtils.getProperty("redis.host"),
                Integer.parseInt(ConfigUtils.getProperty("redis.port")),
                Integer.parseInt(ConfigUtils.getProperty("redis.connection.timeout")),
                ConfigUtils.getProperty("redis.password")
        );*/
    }
}
