package cn.xiaoyaoji.service.biz.project.service;

import cn.xiaoyaoji.service.biz.project.bean.ProjectUser;
import cn.xiaoyaoji.service.biz.project.mapper.ProjectUserMapper;
import cn.xiaoyaoji.service.common.AbstractCurdService;
import cn.xiaoyaoji.source.mapper.CurdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 *
 * @author: zhoujingjie
 * Date: 2018/9/18
 */
@Service
public class ProjectUserService implements AbstractCurdService<ProjectUser> {
    @Autowired
    private ProjectUserMapper projectUserMapper;


    @Override
    public CurdMapper<ProjectUser> getMapper() {
        return projectUserMapper;
    }


    public boolean getUserEditable(String projectId, String docId) {
        Integer editable = projectUserMapper.getUserEditable(projectId, docId);
        return editable != null && editable == 1;
    }
    public int delete(String projectId,String userId){
        return projectUserMapper.deleteByProjectUser(projectId,userId);
    }

    public boolean checkUserExists(String projectId,String userId){
        return projectUserMapper.countByProjectUser(projectId,userId);
    }


    /**
     * 更新常用
     * @param commonlyUsed  1:是。0:否
     */
    public int updateCommonlyUsed(String projectId, String userId, int commonlyUsed){
        return projectUserMapper.updateCommonlyUsed(projectId,userId,commonlyUsed);
    }

    public int updateEditable(String projectId, String userId, int editable){
        return projectUserMapper.updateEditable(projectId,userId,editable);
    }

}
