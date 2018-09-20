package cn.xiaoyaoji.service.integration.file;

import cn.xiaoyaoji.service.util.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件操作管理器
 *
 * @author zhoujingjie
 *         created on 2017/7/1
 */
public class FileManager {
    private static FileProvider fileProvider;
    private static Logger logger = LoggerFactory.getLogger(FileManager.class);

    static {
        String uploadProviderClasss = ConfigUtils.getProperty("file.upload.provider");
        try {
            fileProvider = (FileProvider) Class.forName(uploadProviderClasss).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private FileManager() {
    }

    public static FileProvider getFileProvider() {
        return fileProvider;
    }

}
