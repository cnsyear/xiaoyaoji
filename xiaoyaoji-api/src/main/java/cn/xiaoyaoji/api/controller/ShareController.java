package cn.xiaoyaoji.api.controller;

import cn.xiaoyaoji.api.base.Session;
import cn.xiaoyaoji.service.annotations.Ignore;
import cn.xiaoyaoji.service.biz.doc.bean.Doc;
import cn.xiaoyaoji.service.biz.doc.service.DocService;
import cn.xiaoyaoji.service.biz.project.bean.Share;
import cn.xiaoyaoji.service.biz.project.service.ShareService;
import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.service.util.AssertUtils;
import cn.xiaoyaoji.service.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 分享
 *
 * @author zhoujingjie
 *         created on 2017/8/24
 */
@RequestMapping("/share")
@RestController
public class ShareController {

    @Autowired
    private ShareService shareService;

    /**
     * 查询项目的所有分享
     * @param projectId
     * @param user
     * @return
     */
    @GetMapping("/project/{projectId}")
    public Object getShares(@PathVariable("projectId") String projectId, @Session User user) {
        //todo 检查权限
        return shareService.getSharesByProjectId(projectId);
    }

    /**
     * 删除
     *
     * @param id
     * @param user
     * @return
     */
    @DeleteMapping("/{id}")
    public int delete(@PathVariable("id") String id, @Session User user) {
        //todo 检查是否有项目权限
        Share share = shareService.findOne(id);
        AssertUtils.notNull(share, "无效ID");
        int rs = shareService.deleteByPrimaryKey(id);
        AssertUtils.isTrue(rs > 0, "删除失败");
        return rs;
    }

    /**
     * 修改
     *
     * @param id
     * @param password
     * @param user
     * @return
     */
    @PostMapping("/{id}")
    public int update(@PathVariable("id") String id, @RequestParam("password") String password, @Session User user) {
        Share share = shareService.findOne(id);
        AssertUtils.notNull(share, "无效ID");
        //todo 检查是否有项目权限
        Share temp = new Share();
        temp.setId(share.getId());
        temp.setPassword(password);
        int rs = shareService.updateSelective(temp);
        AssertUtils.isTrue(rs > 0, "操作失败");
        return rs;
    }

    /**
     * 新增
     *
     * @param share
     * @param user
     * @return
     */
    @PostMapping
    public int create(Share share, @Session User user) {
        AssertUtils.notNull(share.getName(), "分享名称不能为空");
        AssertUtils.notNull(share.getProjectId(), "missing projectId");
        share.setId(StringUtils.id());
        //todo 检查是否有项目权限
        share.setUserId(user.getId());
        int rs = shareService.insert(share);
        AssertUtils.isTrue(rs > 0, "操作失败");
        return rs;
    }
}
