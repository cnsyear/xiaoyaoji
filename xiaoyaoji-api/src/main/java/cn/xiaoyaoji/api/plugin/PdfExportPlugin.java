package cn.xiaoyaoji.api.plugin;

import cn.xiaoyaoji.service.biz.doc.bean.Doc;
import cn.xiaoyaoji.service.biz.project.bean.Project;
import cn.xiaoyaoji.service.plugin.ExportPlugin;
import cn.xiaoyaoji.service.spi.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
 * //todo 暂时手动注册,所有插件应该改成启动或者运行时注册
 *
 * @author: zhoujingjie
 * Date: 2018/9/25
 */
@Component
public class PdfExportPlugin implements ExportPlugin {
    @Autowired
    private ExportService exportService;

    @PostConstruct
    public void init() {
        exportService.register("cn.xiaoyaoji.export.pdf", this);
    }

    /**
     * 导出
     *
     * @param project  项目
     * @param docs     文档列表
     * @param request  req
     * @param response resp
     */
    @Override
    public void export(Project project, List<Doc> docs, HttpServletRequest request, HttpServletResponse response) {

    }
}
