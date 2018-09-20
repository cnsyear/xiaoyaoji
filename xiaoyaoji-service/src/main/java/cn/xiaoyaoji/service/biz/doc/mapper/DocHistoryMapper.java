package cn.xiaoyaoji.service.biz.doc.mapper;

import cn.xiaoyaoji.service.biz.doc.bean.DocHistory;
import cn.xiaoyaoji.source.mapper.CurdMapper;
import org.apache.ibatis.annotations.Delete;
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
public interface DocHistoryMapper extends CurdMapper<DocHistory> {


    /**
     * 查询文档历史记录
     *
     * @param docId 文档id
     * @return 历史记录
     */
    @Select("select h.id,h.name,h.comment,h.docId,h.projectId,h.createTime,u.nickname userName from doc_history h\n" +
            "left join user u on u.id = h.userId \n" +
            "where h.docId=#{docId}\n" +
            "order by createTime desc limit 20 ")
    List<DocHistory> selectDocHistoryByDocId(String docId);

    /**
     * 删除超过限制的文档历史
     *
     * @param docId 文档id
     * @param num   数量
     * @return
     */
    @Delete("delete from doc_history where docId = ? and id not in (\n" +
            "select id from (select id from doc_history where docId = #{docId} order by createTime desc limit #{num} ) t\n" +
            ")")
    int deleteThanLimit(String docId, int num);
}
