package cn.xiaoyaoji.service.biz.user.mapper;

import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.source.mapper.CurdMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
@Mapper
public interface UserMapper extends CurdMapper<User> {

    @Select("select nickname from user where id = #{userId}")
    String selectNickName(String userId);

    /**
     * 修改密码
     *
     * @param email    邮箱
     * @param password 新密码
     */
    @Update("update user set password=#{password} where email=#{email}")
    int updatePasswordByEmail(String email, String password);

    @Select("select * from user where email=#{email}")
    User selectByEmail(String email);

    @Select("select count(id) from user where email=#{email}")
    int countByEmail(String email);

    @Select("select id,email,nickname from user where  id != #{excludeId} and instr(nickname , #{nickName})>0 order by length(nickname) asc limit 5")
    List<User> selectUsersByNickName(String nickName, String excludeId);

    @Select("select u.id,u.nickname,avatar,u.email from user u \n"
            + "where u.id in (\n" +
            "select userId from project_user where projectId in (\n" +
            "select projectId from project_user where userId=#{userId}\n" +
            ")\n" +
            ")")
    List<User> selectProjectRelationUser(String userId);

    @Select("select u.id,u.nickname,u.avatar,u.email,pu.editable from user u left join project_user pu on pu.userId=u.id where pu.projectId=#{projectId}")
    List<User> selectByProjectId(String projectId);
}
