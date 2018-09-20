package cn.xiaoyaoji;

import cn.xiaoyaoji.service.ServiceConfig;
import cn.xiaoyaoji.service.util.ConfigUtils;
import cn.xiaoyaoji.service.util.PluginUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import javax.servlet.ServletContext;

/**
 * @author zhoujingjie
 * @date 2016-07-26
 */
@Import(ServiceConfig.class)
@SpringBootApplication(scanBasePackages = "cn.xiaoyaoji")
public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    //todo 清理回收站超过30天的项目
    public static void started(ServletContext servletContext) {
        //MessageBus.instance().register(new MessageNotify());
        initializePlugins(servletContext);
    }

    /**
     * 初始化插件
     *
     * @param servletContext
     */
    private static void initializePlugins(ServletContext servletContext) {
        try {
            String outputURI = servletContext.getRealPath(PluginUtils.getPluginSourceDir());

            String pluginsDir = PluginUtils.getPluginDir();
            //如果为空则与sourcedir 同目录
            if (pluginsDir == null || pluginsDir.length() == 0) {
                pluginsDir = outputURI;
            }
            PluginUtils.extractPlugins(pluginsDir, outputURI);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


}
