package cn.xiaoyaoji.plugin.doc.global;

import cn.xiaoyaoji.core.plugin.ProjectGlobalPlugin;
import cn.xiaoyaoji.core.util.JsonUtils;
import cn.xiaoyaoji.data.DataFactory;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 * 　　┃　　　┃神兽保佑
 * 　　┃　　　┃代码无BUG！
 * 　　┃　　　┗━━━┓
 * 　　┃　　　　　　　┣┓
 * 　　┃　　　　　　　┏┛
 * 　　┗┓┓┏━┳┓┏┛
 * 　　　┃┫┫　┃┫┫
 * 　　　┗┻┛　┗┻┛
 *
 * @author zhoujingjie
 * Date 2018-05-03
 */
public class GlobalArgsPlugin extends ProjectGlobalPlugin {
    private String tableName="plugin_global_args";

    /**
     * @return 编辑页面
     */
    @Override
    public String getEditPage() {
        return "global-args/edit.jsp";
    }

    /**
     * @return 查看页面
     */
    @Override
    public String getViewPage() {
        return "global-args/view.jsp";
    }

    /**
     * 插件初始化
     */
    @Override
    public void init() {
        if(!DataFactory.instance().checkTableExists(tableName)){
            DataFactory.instance().process((connection, qr) -> qr.update(connection,"CREATE TABLE "+tableName+" (\n" +
                    "`id`  int NOT NULL AUTO_INCREMENT ,\n" +
                    "`projectId`  char(12) NULL ,\n" +
                    "`content`  varchar(10000) CHARACTER SET utf8 NULL DEFAULT '' COMMENT '内容，json格式' ,\n" +
                    "PRIMARY KEY (`id`),\n" +
                    "UNIQUE INDEX `projectId` (`projectId`) \n" +
                    ")\n" +
                    "DEFAULT CHARACTER SET=utf8\n" +
                    "COMMENT='全局请求参数表'\n" +
                    ";\n" +
                    "\n"));
        }
    }

    private GlobalArgs getByProjectId(String projectId) {
        return DataFactory.instance().process((connection, qr) -> {
            return qr.query(connection, "select id,projectId,content from " + tableName + " where projectId=?", new BeanHandler<>(GlobalArgs.class), projectId);
        });
    }

    @Override
    public Object httpRequest(String path, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if ("/get".equals(path)) {
            String projectId = request.getParameter("projectId");
            return getByProjectId(projectId);
        } else if ("/update".equals(path)) {
            String content = request.getParameter("content");
            String projectId = request.getParameter("projectId");
            DataFactory.instance().process((connection, qr) -> qr.update(connection, "insert into " + tableName + " (projectId,content) values(?,?) on duplicate key update content=?", projectId, content, content));
            return true;
        }
        throw new UnsupportedOperationException("不支持该地址" + path);
    }
    /**
     * 获取json数据
     *
     * @param projectId 项目id
     * @return json数据
     */
    @Override
    public String getJsonData(String projectId) {
        GlobalArgs temp = getByProjectId(projectId);
        if(temp != null){
            return JsonUtils.toString(temp.getContent());
        }
        return null;
    }
}
