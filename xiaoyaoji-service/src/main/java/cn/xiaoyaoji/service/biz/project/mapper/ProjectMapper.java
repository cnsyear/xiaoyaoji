package cn.xiaoyaoji.service.biz.project.mapper;

import cn.xiaoyaoji.service.biz.project.bean.Project;
import cn.xiaoyaoji.source.mapper.CurdMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
public interface ProjectMapper extends CurdMapper<Project> {

    /**
     * 查询对应权限的项目数量
     *
     * @param projectId 项目id
     * @return 数量
     */
    @Select("select count(1) from project where id=#{projectID} and permission=1")
    int countWithPermission(String projectId);

    @Select("select id from project where status=1 and permission=1 order by createTime desc")
    List<String> selectAllValidIds();


    @Select("select DISTINCT p.*,u.nickname userName,pu.editable,pu.commonlyUsed from project " +
            " p left join user u on u.id = p.userId " +
            " left join project_user pu on pu.projectId = p.id " +
            "   where ( pu.userId=#{userId}) and p.status=#{status} " +
            " order by createTime desc ")
    List<Project> selectByUserId(String userId, int status);
}
