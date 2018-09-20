package cn.xiaoyaoji.api.extension.file;

import cn.xiaoyaoji.service.integration.file.AbstractFileProvider;
import cn.xiaoyaoji.service.util.ConfigUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 七牛云
 * //todo 该功能做成插件
 *
 * @author zhoujingjie
 *         created on 2017/5/18
 */
public class QiniuFileProvider extends AbstractFileProvider {
    private Logger logger = LoggerFactory.getLogger(QiniuFileProvider.class);

    /**
     * 上传
     *
     * @param path  文件路径
     * @param bytes 文件流
     * @return 上传后的文件夹名称
     */
    @Override
    public String upload(String path, byte[] bytes) throws IOException {
        Auth auth = Auth.create(ConfigUtils.getQiniuAccessKey(), ConfigUtils.getQiniuSecretKey());
        UploadManager uploadManager = new UploadManager(getConfiguration());
        try {
            String token = auth.uploadToken(ConfigUtils.getBucketURL(), path, 3600, new StringMap().put("insertOnly", 0));
            Response res = uploadManager.put(bytes, path, token);
            String result = res.bodyString();
            logger.debug("qiniuUpload:" + result);
            if (result.contains("\"key\"")) {
                return path;
            }
            throw new IOException(result);
        } catch (QiniuException e) {
            Response r = e.response;
            throw new IOException(r.bodyString());
        }
    }

    private Configuration getConfiguration(){
        Configuration config = new Configuration();
        return config;
    }


    /**
     * 删除文件
     *
     * @param path 文件路径
     */
    @Override
    public boolean delete(String path) throws IOException {
        Auth auth = Auth.create(ConfigUtils.getQiniuAccessKey(), ConfigUtils.getQiniuSecretKey());

        BucketManager bucketManager = new BucketManager(auth, getConfiguration());
        try {
            bucketManager.delete(ConfigUtils.getBucketURL(), path);
            return true;
        } catch (QiniuException e) {
            Response r = e.response;
            throw new IOException(r.bodyString());
        }
    }
}
