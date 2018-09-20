package cn.xiaoyaoji.api.extension.cache.factory;

import cn.xiaoyaoji.api.extension.cache.provider.MemoryCacheProvider;
import cn.xiaoyaoji.service.integration.cache.CacheProvider;
import cn.xiaoyaoji.service.integration.cache.CacheFactory;

/**
 * @author zhoujingjie
 *         created on 2017/5/18
 */
public class DefaultCacheFactory implements CacheFactory {
    @Override
    public CacheProvider create() {
        return new MemoryCacheProvider();
    }
}
