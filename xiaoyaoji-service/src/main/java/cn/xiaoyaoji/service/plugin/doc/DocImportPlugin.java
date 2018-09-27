package cn.xiaoyaoji.service.plugin.doc;

import cn.xiaoyaoji.service.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhoujingjie
 *         created on 2017/7/1
 */
public abstract class DocImportPlugin extends Plugin {

    public abstract void doImport(String fileName,InputStream file,String userId,String projectId,String parentId) throws  IOException;

}
