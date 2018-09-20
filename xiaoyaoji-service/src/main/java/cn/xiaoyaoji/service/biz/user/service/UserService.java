package cn.xiaoyaoji.service.biz.user.service;

import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.service.biz.user.bean.UserThirdParty;
import cn.xiaoyaoji.service.biz.user.mapper.UserMapper;
import cn.xiaoyaoji.service.biz.user.mapper.UserThirdPartyMapper;
import cn.xiaoyaoji.service.common.AbstractCurdService;
import cn.xiaoyaoji.service.util.AssertUtils;
import cn.xiaoyaoji.source.mapper.Criteria;
import cn.xiaoyaoji.source.mapper.CurdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: zhoujingjie
 * @Date: 17/5/28
 */
@Service
public class UserService implements AbstractCurdService<User> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserThirdPartyMapper userThirdPartyMapper;
    @Autowired
    private FindPasswordService findPasswordService;

    @Override
    public CurdMapper<User> getMapper() {
        return userMapper;
    }

    public User getUser(String id) {
        User user = getMapper().findOne(id);
        if (user != null) {
            userThirdPartyMapper.selectTypesByUserId(id);
        }
        return user;
    }


    public int updateNewPassword(final String findPasswordId, final String email, final String newPassword) {
        if (findPasswordService.checkValidAndSetInvalid(findPasswordId)) {
            return userMapper.updatePasswordByEmail(email, newPassword);
        }
        return 0;
    }


    public User getByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    public boolean checkEmailExists(String email) {
        return userMapper.countByEmail(email) > 0;
    }


    public List<User> searchUsers(String nickName, String excludeId) {
        return userMapper.selectUsersByNickName(nickName, excludeId);
    }


    public List<User> getProjectRelationUser(String userId) {
        return userMapper.selectProjectRelationUser(userId);
    }

    public List<User> getByProjectId(String projectId) {
        return userMapper.selectByProjectId(projectId);
    }


    public int bindUserWithThirdParty(UserThirdParty userThirdParty) {
        User user = userMapper.findOne(userThirdParty.getUserId());
        AssertUtils.notNull(user, "无效用户");
        //检查是否绑定
        userThirdPartyMapper.countMore(Criteria.build().eq("userId", user.getId()).eq("type", userThirdParty.getType()));
        //todo 重新整理当前逻辑
        return 0;
    }

    public User getByEmailPassword(String email,String password){
        User user = userMapper.selectByEmail(email);
        if(user != null && password.equals(user.getPassword())){
            return user;
        }
        return null;
    }

    public int deleteThirdly(String userId,String pluginId){
        return userThirdPartyMapper.deleteType(userId,pluginId);
    }
}

