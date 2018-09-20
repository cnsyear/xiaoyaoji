package cn.xiaoyaoji.service.biz.project.mapper;

import cn.xiaoyaoji.service.biz.project.bean.ProjectUser;
import cn.xiaoyaoji.source.mapper.CurdMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
public interface ProjectUserMapper extends CurdMapper<ProjectUser> {

    @Select("select editable from project_user where projectId=#{projectId} and userId=#{userId} limit 1")
    Integer getUserEditable(String projectId, String docId);

    @Delete("delete from project_user where projectId=#{projectId} and userId=#{userId}")
    int deleteByProjectUser(String projectId, String userId);

    @Select("select count(id) from project_user where projectId=#{projectId} and userId=#{userId}")
    boolean countByProjectUser(String projectId, String userId);


    /**
     * 更新常用
     * @param commonlyUsed  1:是。0:否
     */
    @Update("update project_user set commonlyUsed=#{commonlyUsed} where projectId=#{projectId} and userId=#{userId}")
    int updateCommonlyUsed(String projectId, String userId, int commonlyUsed);


    @Update("update project_user set editable=#{editable} where projectId=#{projectId} and userId=#{userId}")
    int updateEditable(String projectId, String userId, int editable);
}
