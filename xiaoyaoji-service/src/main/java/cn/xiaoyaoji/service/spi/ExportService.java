package cn.xiaoyaoji.service.spi;

import cn.xiaoyaoji.service.plugin.ExportPlugin;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
 * 导出类服务
 *
 * @author: zhoujingjie
 * Date: 2018/9/25
 */
@Service
public class ExportService implements PluginService<ExportPlugin> {

    private Map<String, ExportPlugin> exportPluginMap = new HashMap<>();

    /**
     * 注册插件
     *
     * @param pluginId     插件id
     * @param exportPlugin 插件类
     */
    @Override
    public ExportPlugin register(String pluginId, ExportPlugin exportPlugin) {
        exportPluginMap.put(pluginId, exportPlugin);
        return exportPlugin;
    }


    /**
     * 获取插件
     *
     * @param pluginId 插件id
     * @return cn.xiaoyaoji.service.plugin.ExportPlugin
     */
    @Override
    public ExportPlugin getPlugin(String pluginId) {
        return exportPluginMap.get(pluginId);
    }
}
