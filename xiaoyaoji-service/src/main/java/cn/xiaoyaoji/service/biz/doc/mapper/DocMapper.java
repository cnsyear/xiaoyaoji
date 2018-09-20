package cn.xiaoyaoji.service.biz.doc.mapper;

import cn.xiaoyaoji.service.biz.doc.bean.Doc;
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
public interface DocMapper extends CurdMapper<Doc> {


    /**
     * 搜索文档
     *
     * @param projectId 项目id
     * @param text      搜索内容
     * @return id, name
     */
    @Select("select id,name from doc where projectId=#{projectId} and (instr(name,#{text})>0  or instr(content,#{text})>0) order by sort asc ,createTime desc")
    List<Doc> selectDoc(String projectId, String text);


    @Select("select id,name from doc where projectId=#{projectId} and parentId=#{parentId} order by sort asc")
    List<Doc> selectByParentId(String projectId, String parentId);

    /**
     * 假删除
     *
     * @param id 文档id
     * @return rows
     */
    @Update("update doc set status =0 where id = #{id}")
    int fakeDelete(String id);



    @Select("<script>select group_concat(name) from doc where id in (<foreach collection=\"docIds\" item=\"item\" separator=\",\">#{item}</foreach>) </script>")
    String selectNameFromIds(List<String> docIds);


    @Select("select id,name,sort,type,parentId,projectId,if(#{searchContent},content,null) content from doc where projectId=#{projectId}")
    List<Doc> selectDocsByProjectId(String projectId, boolean searchContent);

    @Select("select id from doc d where (select count(1) from doc_history where docId = d.id) > #{num}")
    List<String> selectRatherThanNumsDocIds(int num);
}
