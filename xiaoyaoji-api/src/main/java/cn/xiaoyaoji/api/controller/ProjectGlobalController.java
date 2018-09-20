package cn.xiaoyaoji.api.controller;

import cn.xiaoyaoji.api.base.Session;
import cn.xiaoyaoji.service.biz.project.service.ProjectGlobalService;
import cn.xiaoyaoji.service.common.Message;
import cn.xiaoyaoji.service.util.AssertUtils;
import cn.xiaoyaoji.service.biz.project.bean.Project;
import cn.xiaoyaoji.service.biz.project.bean.ProjectGlobal;
import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.service.biz.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author: zhoujingjie
 * @Date: 17/4/25
 */
@RestController
@RequestMapping("/project/global")
public class ProjectGlobalController {
    @Autowired
    private ProjectGlobalService projectGlobalService;

    @GetMapping("{projectId}")
    public Object getByProjectId(@PathVariable String projectId) {
        //todo 检查权限
        ProjectGlobal pg = projectGlobalService.getByProjectId(projectId);
        return pg;
    }


    @PostMapping("{projectId}")
    public int save(@PathVariable String projectId, ProjectGlobal pg, @Session User user) {
        //todo 检查权限
        ProjectGlobal temp = projectGlobalService.getByProjectId(projectId);
        AssertUtils.notNull(temp, "全局对象不存在");
        pg.setId(temp.getId());
        pg.setProjectId(projectId);
        //检查权限
        int rs = projectGlobalService.updateSelective(temp);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return rs;
    }
}
