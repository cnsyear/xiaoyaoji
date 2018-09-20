package cn.xiaoyaoji.service.common;

import cn.xiaoyaoji.source.mapper.Criteria;
import cn.xiaoyaoji.source.mapper.CurdMapper;

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
 * Date: 18/6/18
 */
public interface AbstractCurdService<T> {

    CurdMapper<T> getMapper();

    default int delete(T bean){
        return getMapper().delete(bean);
    }

    default int deleteByPrimaryKey(Object key){
        return getMapper().deleteByPrimaryKey(key);
    }

    default int update(T bean){
            return getMapper().update(bean);
    }

    default int updateSelective(T bean){
        return getMapper().updateSelective(bean);
    }

    default int insertUseGeneratedKeys(T bean){
        return getMapper().insertUseGeneratedKeys(bean);
    }

    default int insert(T bean){
        return getMapper().insert(bean);
    }

    default int countMore(Criteria criteria){
        return getMapper().countMore(criteria);
    }

    default List<T> findMore(Criteria criteria){
        return getMapper().findMore(criteria);
    }

    default T findOne(Object id){
        return getMapper().findOne(id);
    }
}
