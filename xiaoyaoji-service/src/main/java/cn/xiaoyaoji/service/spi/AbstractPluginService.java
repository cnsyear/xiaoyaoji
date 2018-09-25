package cn.xiaoyaoji.service.spi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
 *
 * @author: zhoujingjie
 * Date: 2018/9/25
 */
public abstract class AbstractPluginService<T> implements PluginService<T> {

    protected Map<String, T> pluginMap = new ConcurrentHashMap<>();

    /**
     * 注册插件
     *
     * @param pluginId 插件id
     * @param plugin   插件类
     * @return T
     */
    @Override
    public T register(String pluginId, T plugin) {
        pluginMap.put(pluginId, plugin);
        return plugin;
    }

    /**
     * 注销插件
     *
     * @param pluginId 插件id
     * @return true/false
     */
    @Override
    public T destroy(String pluginId) {
        return pluginMap.remove(pluginId);
    }

    /**
     * 获取插件类
     *
     * @param pluginId 插件id
     * @return T
     */
    @Override
    public T getPlugin(String pluginId) {
        return pluginMap.get(pluginId);
    }
}
