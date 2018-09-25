package cn.xiaoyaoji.api.controller;

import cn.xiaoyaoji.api.base.Session;
import cn.xiaoyaoji.service.annotations.Ignore;
import cn.xiaoyaoji.service.biz.doc.bean.DocHistory;
import cn.xiaoyaoji.service.biz.doc.service.DocHistoryService;
import cn.xiaoyaoji.service.biz.doc.view.DocType;
import cn.xiaoyaoji.service.common.HashMapX;
import cn.xiaoyaoji.service.Message;
import cn.xiaoyaoji.service.plugin.PluginInfo;
import cn.xiaoyaoji.service.plugin.PluginManager;
import cn.xiaoyaoji.service.spi.ExportService;
import cn.xiaoyaoji.service.util.AssertUtils;
import cn.xiaoyaoji.service.util.StringUtils;
import cn.xiaoyaoji.service.biz.doc.bean.Doc;
import cn.xiaoyaoji.service.biz.project.bean.Project;
import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.service.biz.doc.event.DocUpdatedEvent;
import cn.xiaoyaoji.service.biz.doc.service.DocService;
import cn.xiaoyaoji.service.biz.project.service.ProjectService;
import cn.xiaoyaoji.service.plugin.Event;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author: zhoujingjie
 * @Date: 16/7/13
 */
@RestController
@RequestMapping(value = {"/doc"})
public class DocController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private DocService docService;
    @Autowired
    private DocHistoryService docHistoryService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ExportService exportService;

    /**
     * 新增
     *
     * @return
     */
    @PostMapping
    public String createDoc(User user, Doc doc) {
        //todo 检查创建权限
        if (Strings.isNullOrEmpty(doc.getParentId())) {
            doc.setParentId("0");
        }
        doc.setId(StringUtils.id());
        if (Strings.isNullOrEmpty(doc.getName())) {
            doc.setName("默认文档");
        }
        //默认markdown
        if (Strings.isNullOrEmpty(doc.getType())) {
            doc.setType(DocType.SYS_DOC_MD.getTypeName());
        }
        doc.setLastUpdateTime(new Date());
        doc.setCreateTime(new Date());
        AssertUtils.notNull(doc.getProjectId(), "missing projectId");
        AssertUtils.notNull(doc.getParentId(), "missing parentId");
        int rs = docService.createDoc(doc);
        AssertUtils.isTrue(rs > 0, "增加失败");
        return doc.getId();
    }


    /**
     * 更新
     *
     * @param id id
     * @return
     */
    @PostMapping("{id}")
    public int update(@PathVariable("id") String id, Doc doc, User user, String comment) {
        //todo 检查修改权限
        AssertUtils.notNull(id, "missing id");
        Doc temp = docService.findOne(id);
        AssertUtils.notNull(temp, "文档不存在或已删除");
        temp.setId(null);
        doc.setId(id);
        doc.setCreateTime(null);
        if (Strings.isNullOrEmpty(doc.getName())) {
            doc.setName(null);
        }
        int rs = docService.updateSelective(doc);
        AssertUtils.isTrue(rs > 0, "修改失败");
        applicationEventPublisher.publishEvent(new DocUpdatedEvent(temp, doc, user, comment));
        return rs;
    }


    /**
     * 获取历史修改记录
     *
     * @param docId
     * @return
     */
    @RequestMapping("/history/{docId}")
    public Object getHistory(@PathVariable String docId) {
        return docHistoryService.findOne(docId);
    }


    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public int delete(@PathVariable("id") String id, User user) {
        //todo 检查删除权限
        AssertUtils.notNull(id, "missing id");
        int rs = docService.deleteDoc(id);

        AssertUtils.isTrue(rs > 0, "删除失败");
        return rs;
    }

    /**
     * 修改文档顺序
     *
     * @param id       文档id
     * @param parentId 父级id
     * @param sorts    id-sort
     */
    @PostMapping("{id}/sort")
    public Object sort(@PathVariable("id") String id,
                       @RequestParam("parentId") String parentId,
                       @RequestParam("sorts") String sorts
    ) {
        AssertUtils.notNull(id, "参数为空");
        AssertUtils.notNull(parentId, "参数为空");
        AssertUtils.notNull(sorts, "参数为空");
        //更新parentId
        Doc doc = new Doc();
        doc.setId(id);
        doc.setParentId(parentId);
        docService.updateSelective(doc);

        String[] idsorts = sorts.split(",");
        int rs = docService.updateSort(idsorts);
        AssertUtils.notNull(rs > 0, Message.OPER_ERR);
        return true;
    }


    /**
     * 预览文档
     *
     * @param docId
     * @param docHistoryId
     * @param user
     * @return
     */
    @GetMapping("{docId}")
    @Ignore
    public Object getDetails(@PathVariable String docId,
                          @RequestParam(value = "docHistoryId", required = false) String docHistoryId, @Session User user
    ) {
        //todo  检查是否有访问权限
        AssertUtils.notNull(docId, "参数丢失");
        Doc doc = null;
        if (!Strings.isNullOrEmpty(docHistoryId)) {
            DocHistory history = docHistoryService.findOne(docHistoryId);
            AssertUtils.notNull(history,"数据不存在");
            doc = new Doc();
            BeanUtils.copyProperties(history, doc);
            AssertUtils.isTrue(doc.getId().equals(docId), "数据无效");
        } else {
            doc = docService.findOne(docId);
        }
        AssertUtils.notNull(doc, "文档不可见或已删除");
        if (doc.getType().equals(DocType.SYS_SHORT_CUT.getTypeName())) {
            return new ModelAndView("redirect:/doc/" + doc.getContent());
        }
        //获取project
        Project project = projectService.findOne(doc.getProjectId());
        AssertUtils.notNull(project, "项目不存在或者无访问权限");
        AssertUtils.isTrue(project.getStatus() == 1, "项目状态无效");
        if (Strings.isNullOrEmpty(doc.getType())) {
            doc.setType(DocType.SYS_DOC_RICH_TEXT.getTypeName());
        }

        boolean editPermission = false;
        if (user != null) {
            //访问权限
            //todo 检查是否有编辑权限
        }

        List<PluginInfo> pluginInfos = PluginManager.getInstance().getPlugins(Event.DOC_EV);
        PluginInfo pluginInfo = null;
        for (PluginInfo info : pluginInfos) {
            if (doc.getType().equals(info.getId())) {
                pluginInfo = info;
                break;
            }
        }
        return new HashMapX<>().append("doc", doc).append("editPermission", editPermission);
    }

    /**
     * 文档搜索
     *
     * @param text      搜索内容
     * @param projectId 项目
     * @param user
     * @return
     */
    @Ignore
    @GetMapping("/search")
    public Object search(@RequestParam String text, @RequestParam("projectId") String projectId, User user) {
        //todo 检查是否有访问权限
        List<Doc> docs = docService.searchDocs(text, projectId);
        return new HashMapX<>()
                .append("docs", docs);
    }

    /**
     * @param projectId
     * @param user
     * @return
     */
    @GetMapping("/list/{projectId}")
    public Object getDocs(@PathVariable("projectId") String projectId, User user) {
        //todo 检查访问权限
        return docService.getProjectDocs(projectId);
    }

    /**
     * 复制文档
     *
     * @param projectId 项目id
     * @param docId     文档id
     * @param user
     * @return rs
     */
    @PostMapping("/copy")
    public Object copy(@RequestParam("projectId") String projectId,
                       @RequestParam(value = "toProjectId", required = false) String toProjectId,
                       @RequestParam("docId") String docId, User user) {
        //todo 检查访问权限
        if (Strings.isNullOrEmpty(toProjectId) || projectId.equals(toProjectId)) {
            toProjectId = null;
        } else {
            //todo 检查是否有另外一个项目的编辑权限
        }
        int rs = docService.copyDoc(docId, toProjectId);
        AssertUtils.isTrue(rs > 0, "操作失败");
        return rs;
    }


    /**
     * 创建软链接
     *
     * @param projectId 项目id
     * @param docId     文档id
     * @param user
     * @return rs
     */
    @PostMapping("/shortcut")
    public Object shortcut(@RequestParam("projectId") String projectId,
                           @RequestParam(value = "toProjectId", required = false) String toProjectId,
                           @RequestParam("docId") String docId, User user) {
        //todo 检查是否有编辑权限
        if (Strings.isNullOrEmpty(toProjectId) || projectId.equals(toProjectId)) {
            toProjectId = null;
        } else {
            //todo 检查是否有编辑权限
        }
        Doc source = docService.findOne(docId);
        AssertUtils.notNull(source, "无效文档");
        Doc newDoc = new Doc();
        if (toProjectId == null) {
            toProjectId = source.getProjectId();
        }
        newDoc.setProjectId(toProjectId);
        newDoc.setId(StringUtils.id());
        newDoc.setContent(docId);
        newDoc.setType(DocType.SYS_SHORT_CUT.getTypeName());
        newDoc.setLastUpdateTime(new Date());
        newDoc.setCreateTime(new Date());
        newDoc.setName(source.getName());
        newDoc.setSort(newDoc.getSort() == null ? 1 : newDoc.getSort() + 1);
        if (toProjectId.equals(source.getProjectId())) {
            newDoc.setParentId(source.getParentId());
        } else {
            newDoc.setParentId("0");
        }
        docService.createDoc(newDoc);
        return 1;
    }


    /**
     * 查询顶级doc
     *
     * @param projectId
     * @param user
     * @return
     */
    @GetMapping("/root/{projectId}")
    public Object getRootDocs(@PathVariable("projectId") String projectId, User user) {
        //todo 检查是否有访问权限
        List<Doc> docs = docService.getByParentId(projectId, "0");
        return new HashMapX<>()
                .append("docs", docs);
    }
}
