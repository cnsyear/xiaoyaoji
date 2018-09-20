package cn.xiaoyaoji.source.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 * 　　┃　　　┃神兽保佑
 * 　　┃　　　┃代码无BUG！
 * 　　┃　　　┗━━━┓
 * 　　┃　　　　　　　┣┓
 * 　　┃　　　　　　　┏┛
 * 　　┗┓┓┏━┳┓┏┛
 * 　　　┃┫┫　┃┫┫
 * 　　　┗┻┛　┗┻┛
 * <p>
 * CRUD 通用mapper
 *
 * @author zhoujingjie
 *         Date 2017/11/23
 */
public interface CurdMapper<T> {

    /**
     * 单个查询
     *
     * @param id
     * @return
     */
    @SelectProvider(method = "findOne", type = CurdBuilder.class)
    T findOne(Object id);

    /**
     * 按条件查询
     *
     * @param criteria
     * @return
     */
    @SelectProvider(method = "findMore", type = CurdBuilder.class)
    List<T> findMore(Criteria criteria);

    /**
     * 计算数量
     *
     * @param criteria
     * @return
     */
    @SelectProvider(method = "countMore", type = CurdBuilder.class)
    int countMore(Criteria criteria);

    /**
     * 插入单个对象
     *
     * @param bean
     * @return
     */
    @InsertProvider(type = CurdBuilder.class, method = "insert")
    int insert(T bean);


    /**
     * 并且带上主键
     *
     * @return
     */
    @InsertProvider(type = CurdBuilder.class, method = "insertUseGeneratedKeys")
    @Options(useGeneratedKeys = true)
    int insertUseGeneratedKeys(T bean);

    /**
     * 插入多个对象
     *
     * @param beans
     * @return
     */
    /*@InsertProvider(type = cn.xiaoyaoji.source.mapper.CurdBuilder.class, method = "inserts")
    List<T> inserts(List<T> beans);*/

    /**
     * 根据id更新非空数据
     *
     * @param bean
     * @return
     */
    @UpdateProvider(type = CurdBuilder.class, method = "updateSelective")
    int updateSelective(T bean);


    /**
     * 根据id更新所有可更新字段
     *
     * @param bean
     * @return
     */
    @UpdateProvider(type = CurdBuilder.class, method = "update")
    int update(T bean);


    /**
     * 根据主键删除
     *
     * @param key
     * @return
     */
    @DeleteProvider(type = CurdBuilder.class, method = "deleteByPrimaryKey")
    int deleteByPrimaryKey(Object key);

    /**
     * 根据非空字段删除
     *
     * @param bean
     * @return
     */
    @DeleteProvider(type = CurdBuilder.class, method = "delete")
    int delete(T bean);

    /**
     * 批量删除
     *
     * @param keys
     * @return
     */
    @DeleteProvider(type = CurdBuilder.class, method = "deleteByPrimaryKeys")
    int deleteByPrimaryKeys(List keys);
}
