package cn.xiaoyaoji.service.plugin.doc;

import cn.xiaoyaoji.service.plugin.Plugin;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhoujingjie
 *         created on 2017/7/1
 */
public abstract class DocExportPlugin extends Plugin<DocExportPlugin> {


   public abstract void doExport(String projectId, HttpServletResponse response) throws IOException;

}
