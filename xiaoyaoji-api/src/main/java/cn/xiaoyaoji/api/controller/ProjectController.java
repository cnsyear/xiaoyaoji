package cn.xiaoyaoji.api.controller;

import cn.xiaoyaoji.api.base.Session;
import cn.xiaoyaoji.service.annotations.Ignore;
import cn.xiaoyaoji.service.biz.doc.bean.Doc;
import cn.xiaoyaoji.service.biz.doc.service.DocService;
import cn.xiaoyaoji.service.biz.project.bean.Project;
import cn.xiaoyaoji.service.biz.project.bean.ProjectUser;
import cn.xiaoyaoji.service.biz.project.service.ProjectService;
import cn.xiaoyaoji.service.biz.project.service.ProjectUserService;
import cn.xiaoyaoji.service.biz.project.service.ShareService;
import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.service.biz.user.service.UserService;
import cn.xiaoyaoji.service.Message;
import cn.xiaoyaoji.service.common.HashMapX;
import cn.xiaoyaoji.service.plugin.ExportPlugin;
import cn.xiaoyaoji.service.plugin.ImportPlugin;
import cn.xiaoyaoji.service.plugin.PluginInfo;
import cn.xiaoyaoji.service.plugin.doc.DocImportPlugin;
import cn.xiaoyaoji.service.spi.ExportService;
import cn.xiaoyaoji.service.spi.ImportService;
import cn.xiaoyaoji.service.util.AssertUtils;
import cn.xiaoyaoji.service.util.StringUtils;
import com.google.common.base.Strings;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 项目
 *
 * @author zhoujingjie
 * @date 2016-07-20
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private ExportService exportService;
    @Autowired
    private ImportService importService;
    @Autowired
    private DocService docService;

    /**
     * 项目详情
     *
     * @param id 项目id
     */
    @GetMapping("/{id}")
    public Object details(@PathVariable("id") String id) {
        //todo 检查权限
        return projectService.findOne(id);
    }

    /**
     * 查询会员
     *
     * @param id 项目id
     * @return 成员列表
     */
    @GetMapping("/{id}/member")
    public Object getMembers(@PathVariable("id") String id, @Session User user) {
        //todo 检查权限
        Project project = projectService.findOne(id);
        AssertUtils.notNull(project, Message.PROJECT_NOT_FOUND);
        return new HashMapX<>().append("users", userService.getByProjectId(id));
    }

    /**
     * 导出
     *
     * @param id       项目id
     * @param pluginId 插件id
     * @param docId    文档id
     * @param request  request
     * @param response resp
     * @param user     当前登录用户
     * @return pdfview
     */
    @Ignore
    @GetMapping(value = "/{id}/export/{pluginId}/do")
    public void export(@PathVariable("id") String id, @PathVariable String pluginId,
                       @RequestParam(name = "docId", required = false) String docId,
                       @Session(required = false) User user,
                       HttpServletRequest request, HttpServletResponse response) throws IOException {

        Project project = projectService.findOne(id);
        AssertUtils.notNull(project, Message.PROJECT_NOT_FOUND);
        //ServiceTool.checkUserHasAccessPermission(project, user);
        //todo 检查是否有项目权限
        ExportPlugin plugin = exportService.getPlugin(pluginId);
        AssertUtils.notNull(plugin, "不支持该操作");
        List<Doc> docs = Strings.isNullOrEmpty(docId) ? docService.getByProjectId(id, true) : docService.getByParentId(id, docId);
        plugin.export(project, docs, request, response);
    }

    /**
     * 导入json
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/import/{pluginId}/do")
    public Object projectImport(@PathVariable("pluginId") String pluginId, @Session User user,
                                @RequestParam("file") MultipartFile file
    ) throws IOException {
        ImportPlugin plugin = importService.getPlugin(pluginId);
        AssertUtils.notNull(plugin, "无效插件" + pluginId);
        Project project = plugin.importProject(file.getInputStream());
        if (Strings.isNullOrEmpty(project.getName())) {
            project.setName(FilenameUtils.getBaseName(file.getName()));
        }
        projectService.importProject(project, user.getId());
        return project.getId();
    }


    /**
     * 查询项目列表
     *
     * @param status 状态 ;1:有效;2:无效;3:已删除;4:已存档
     */
    @GetMapping("/list")
    public Object list(@Session User user, @RequestParam(value = "status", defaultValue = "1") int status) {

        return projectService.getProjectsByUserId(user.getId(), status);
    }


    /**
     * 设置是否常用项目
     *
     * @param id
     * @return
     */
    @PostMapping("/{id}/commonly")
    public int updateCommonlyUsed(@PathVariable("id") String id, @Session User user,
                                  @RequestParam int isCommonlyUsed
    ) {
        ////todo 权限验证
        int rs = projectUserService.updateCommonlyUsed(id, user.getId(), isCommonlyUsed);
        AssertUtils.isTrue(rs > 0, "操作失败");
        return rs;
    }

    /**
     * 创建项目
     *
     * @param project 项目对象
     * @return 项目
     */
    @PostMapping
    public Object create(@Session User user, Project project) {
        project.setId(StringUtils.id());
        project.setUserId(user.getId());
        project.setStatus(1);
        if (project.getPermission() == null) {
            project.setPermission(0);
        }
        if (Strings.isNullOrEmpty(project.getName())) {
            project.setName("取个什么名字好呢?");
        }
        int rs = projectService.insert(project);
        return project.getId();
    }


    /**
     * 更新
     *
     * @param project 项目对象
     */
    @PostMapping("/{id}")
    public Object update(@PathVariable("id") String id, @Session User user, Project project) {
        //todo 检查是否有编辑权限
        project.setId(id);
        int rs = projectService.updateSelective(project);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return project.getId();
    }

    /**
     * 项目转让
     *
     * @param id
     * @return
     */
    @PostMapping("/{id}/transfer")
    public Object transfer(@PathVariable("id") String id, @Session User user, @RequestParam String userId) {

        AssertUtils.notNull(userId, "missing userId");
        //todo 检查是否是自己
        Project temp = new Project();
        temp.setId(id);
        temp.setUserId(userId);
        int rs = projectService.updateSelective(temp);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        projectUserService.updateEditable(id, userId, 1);
        return rs;
    }

    /**
     * 删除项目
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public Object delete(@PathVariable("id") String id, @Session User user) {
        Project before = projectService.findOne(id);
        AssertUtils.notNull(before, "无效请求");
        //todo 检查是否是自己项目
        Project temp = new Project();
        temp.setStatus(3);
        temp.setId(id);
        int rs = projectService.updateSelective(temp);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return rs;
    }

    /**
     * 彻底删除
     *
     * @param id 项目id
     */
    @DeleteMapping("/{id}/actual")
    public Object deleteActual(@PathVariable("id") String id, @Session User user) {
        //todo 检查是否是自己项目
        int rs = projectService.deleteByPrimaryKey(id);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return rs;
    }


    /**
     * 邀请成员
     */
    @PostMapping("/{id}/invite")
    public String invite(@PathVariable("id") String id, @Session User user, @RequestParam String userId) {
        //todo 检查是否有编辑权限
        ProjectUser pu = new ProjectUser();
        pu.setId(StringUtils.id());
        pu.setUserId(userId);
        AssertUtils.notNull(pu.getUserId(), "missing userId");
        AssertUtils.isTrue(!projectUserService.checkUserExists(id, pu.getUserId()), "用户已存在该项目中");
        AssertUtils.isTrue(!pu.getUserId().equals(user.getId()), "不能邀请自己");
        pu.setCreateTime(new Date());
        pu.setStatus(1);
        pu.setEditable(0);
        pu.setProjectId(id);
        int rs = projectUserService.insert(pu);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return pu.getId();
    }

    /**
     * 邀请成员
     */
    @PostMapping("/{id}/invite/email")
    @ResponseBody
    public String inviteByEmail(@PathVariable("id") String id, @RequestParam("email") String email, @Session User user) {
        //todo 检查是否有编辑权限
        User temp = userService.getByEmail(email);
        AssertUtils.notNull(temp, "用户不存在");
        String userId = temp.getId();
        AssertUtils.isTrue(userId != null, "该邮箱未注册");
        AssertUtils.isTrue(!userId.equals(user.getId()), "不能邀请自己");
        AssertUtils.isTrue(!projectUserService.checkUserExists(id, userId), "用户已存在该项目中");

        ProjectUser pu = new ProjectUser();
        pu.setId(StringUtils.id());
        pu.setUserId(userId);
        pu.setProjectId(id);
        pu.setEditable(0);
        AssertUtils.notNull(pu.getProjectId(), "missing projectId");
        pu.setStatus(1);
        int rs = projectUserService.insert(pu);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return pu.getId();
    }

    /**
     * 接受邀请
     *
     * @param inviteId 邀请id
     */
    @PostMapping("/{id}/pu/{inviteId}/accept")
    public int acceptInvite(@PathVariable("inviteId") String inviteId) {
        //todo 检查权限
        ProjectUser pu = new ProjectUser();
        pu.setId(inviteId);
        pu.setStatus(1);
        int rs = projectUserService.updateSelective(pu);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return rs;
    }

    /**
     * 拒绝邀请
     */
    @PostMapping("/{id}/pu/{inviteId}/refuse")
    public int acceptRefuse(@PathVariable("inviteId") String inviteId) {
        //todo 检查权限
        ProjectUser pu = new ProjectUser();
        pu.setId(inviteId);
        pu.setStatus(3);
        int rs = projectUserService.updateSelective(pu);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return rs;
    }

    /**
     * 移除成员
     *
     * @param userId userId
     * @param id     projectId
     */
    @DeleteMapping("/{id}/removeMember/{userId}")
    public int removeMember(@PathVariable("id") String id, @PathVariable("userId") String userId, @Session User user) {
        Project project = projectService.findOne(id);
        //todo 检查权限
        AssertUtils.isTrue(!project.getUserId().equals(userId), "不能移除自己");
        AssertUtils.isTrue(user.getId().equals(project.getUserId()), "无操作权限");
        int rs = projectUserService.delete(id, userId);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return rs;
    }

    /**
     * 设置是否可编辑
     *
     * @param projectId 项目id
     * @param userId
     * @param editable
     * @return
     */
    @PostMapping("/{id}/user/{userId}/{editable}")
    @ResponseBody
    public int editProjectEditable(@PathVariable("id") String projectId, @PathVariable("userId") String userId, @PathVariable("editable") int editable,
                                   @Session User user) {
        AssertUtils.isTrue(editable == 1 || editable == 0, "参数错误");
        Project project = projectService.findOne(projectId);
        //todo 检查权限
        AssertUtils.isTrue(!project.getUserId().equals(userId), "项目所有人不能修改自己的权限");
        int rs = projectUserService.updateEditable(projectId, userId, editable);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return rs;
    }

    /**
     * 退出项目
     */
    @DeleteMapping("/{id}/quit")
    public int quit(@PathVariable("id") String id, @Session User user) {
        Project project = projectService.findOne(id);
        AssertUtils.notNull(project, "项目不存在");
        AssertUtils.isTrue(!project.getUserId().equals(user.getId()), "项目所有人不能退出项目");
        int rs = projectUserService.delete(id, user.getId());
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return rs;
    }

    /**
     * 项目归档
     *
     * @param id
     * @param user
     * @return
     */
    @PostMapping("/{id}/archive")
    public int archive(@PathVariable("id") String id, @Session User user) {
        Project project = projectService.findOne(id);
        AssertUtils.notNull(project, "项目不存在");
        AssertUtils.isTrue(project.getUserId().equals(user.getId()), "非项目所有者不能操作");
        Project temp = new Project();
        temp.setId(id);
        temp.setStatus(4);
        int rs = projectService.updateSelective(temp);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return rs;
    }


    /**
     * 查询跟自己相关的项目的用户
     *
     * @return 用户列表
     */
    @GetMapping("users")
    public Object getAllProjectUsers(@Session User user) {
        List<User> users = userService.getProjectRelationUser(user.getId());
        return new HashMapX<>()
                .append("users", users);
    }
}
