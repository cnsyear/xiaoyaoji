package cn.xiaoyaoji.api.controller;

import cn.xiaoyaoji.api.base.Session;
import cn.xiaoyaoji.service.annotations.Ignore;
import cn.xiaoyaoji.service.biz.project.bean.Project;
import cn.xiaoyaoji.service.biz.project.bean.ProjectUser;
import cn.xiaoyaoji.service.biz.project.service.ProjectService;
import cn.xiaoyaoji.service.biz.project.service.ProjectUserService;
import cn.xiaoyaoji.service.biz.project.service.ShareService;
import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.service.biz.user.service.UserService;
import cn.xiaoyaoji.service.common.Message;
import cn.xiaoyaoji.service.plugin.PluginInfo;
import cn.xiaoyaoji.service.plugin.PluginManager;
import cn.xiaoyaoji.service.plugin.doc.DocExportPlugin;
import cn.xiaoyaoji.service.plugin.doc.DocImportPlugin;
import cn.xiaoyaoji.service.util.AssertUtils;
import cn.xiaoyaoji.service.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

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
    private ShareService shareService;
    @Autowired
    private ProjectUserService projectUserService;

    @GetMapping("/{id}/info")
    public ModelAndView detailInfo(@PathVariable("id") String id, User user) {
        Project project = projectService.findOne(id);
        AssertUtils.notNull(project, Message.PROJECT_NOT_FOUND);
        //todo 检查是否有项目权限
        return new ModelAndView("/project/info")
                .addObject("project", project)
                .addObject("pageName", "info")
                ;
    }

    @GetMapping("/{id}")
    public Object details(@PathVariable("id") String id) {
        return projectService.findOne(id);
    }

    /**
     * 查询会员
     *
     * @param id
     * @param user
     * @return
     */
    @GetMapping("/{id}/member")
    public Object detailMember(@PathVariable("id") String id, User user) {
        Project project = projectService.findOne(id);
        AssertUtils.notNull(project, Message.PROJECT_NOT_FOUND);
        //ServiceTool.checkUserIsMember(project, user);
        //todo 检查是否有项目权限
        return userService.getByProjectId(id);
    }

    /**
     * 导出
     *
     * @param id   项目id
     * @param user 当前登录用户
     * @return pdfview
     */
    @Ignore
    @GetMapping(value = "/{id}/export/{pluginId}/do")
    public void export(@PathVariable("id") String id, @PathVariable String pluginId, User user, HttpServletResponse response) throws IOException {

        Project project = projectService.findOne(id);
        AssertUtils.notNull(project, Message.PROJECT_NOT_FOUND);
        //ServiceTool.checkUserHasAccessPermission(project, user);
        //todo 检查是否有项目权限
        PluginInfo<DocExportPlugin> docExportPluginPluginInfo = PluginManager.getInstance().getExportPlugin(pluginId);
        AssertUtils.notNull(docExportPluginPluginInfo, "不支持该操作");
        docExportPluginPluginInfo.getPlugin().doExport(id, response);
    }

    /**
     * 导入json
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/import")
    public Object projectImport(String pluginId, User user,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam(value = "projectId", required = false) String projectId,
                                @RequestParam(value = "parentId", required = false) String parentId
    ) throws IOException {
        PluginInfo<DocImportPlugin> docImportPluginPluginInfo = PluginManager.getInstance().getImportPlugin(pluginId);
        AssertUtils.notNull(docImportPluginPluginInfo, "不支持该操作");
        docImportPluginPluginInfo.getPlugin().doImport(file.getName(), file.getInputStream(), user.getId(), projectId, parentId);
        return true;
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


    @GetMapping("/{id}/shares")
    public Object shares(@PathVariable("id") String id) {

        return shareService.getSharesByProjectId(id);
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
        int rs = projectUserService.updateCommonlyUsed(id, user.getId(), isCommonlyUsed);
        AssertUtils.isTrue(rs > 0, "操作失败");
        return rs;
    }

    /**
     * 创建项目
     *
     * @param user
     * @param project
     * @return
     */
    @PostMapping
    public Object create(@Session User user, Project project) {
        project.setId(StringUtils.id());
        project.setUserId(user.getId());
        project.setStatus(1);
        AssertUtils.notNull(project.getPermission(), "missing permission");
        AssertUtils.notNull(project.getName(), "missing name");
        int rs = projectService.insert(project);
        return project;
    }


    /**
     * 更新
     *
     * @param id
     * @return
     */
    @PostMapping("{id}")
    public Object update(@PathVariable("id") String id, @Session User user, Project project) {
        //todo 检查是否有编辑权限
        project.setId(id);
        int rs = projectService.updateSelective(project);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return rs;
    }

    /**
     * 项目转让
     *
     * @param id
     * @return
     */
    @PostMapping("/{id}/transfer")
    @ResponseBody
    public Object transfer(@PathVariable("id") String id, User user, @RequestParam String userId) {

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
    @ResponseBody
    public Object delete(@PathVariable("id") String id, User user) {
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
     * @param id
     * @param user
     * @return
     */
    @DeleteMapping("/{id}/actual")
    @ResponseBody
    public Object deleteActual(@PathVariable("id") String id, User user) {
        //todo 检查是否是自己项目
        int rs = projectService.deleteByPrimaryKey(id);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return rs;
    }


    /**
     * 邀请成员
     *
     * @param id
     * @return
     */
    @PostMapping("/{id}/invite")
    @ResponseBody
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
     *
     * @param id
     * @return
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
     * @param inviteId
     * @return
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
     * @return
     */
    @DeleteMapping("/{id}/pu/{userId}")
    @ResponseBody
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
    @PostMapping("/{id}/pu/{userId}/{editable}")
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
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}/quit")
    @ResponseBody
    public int quit(@PathVariable("id") String id, @Session User user) {
        Project project = projectService.findOne(id);
        AssertUtils.notNull(project, "project not exists");
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
}
