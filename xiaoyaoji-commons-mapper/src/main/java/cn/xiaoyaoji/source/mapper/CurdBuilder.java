package cn.xiaoyaoji.source.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
 * 动态SQL builder类
 *
 * @author zhoujingjie
 *         Date 2017/11/23
 */
public class CurdBuilder {
    private static Logger logger = LoggerFactory.getLogger(CurdBuilder.class);

    /**
     * 单个查询
     *
     * @param ms
     * @return
     */
    public String findOne(MappedStatement ms) {
        Bean bean = EntityAssistant.getBean(ms);
        StringBuilder sql = searchSegment(bean);
        sql.append(" where ")
                .append(bean.getPrimaryKeyColumnName())
                .append("=#{0}");
        debug(sql);
        return sql.toString();
    }

    private StringBuilder searchSegment(Bean bean) {
        StringBuilder sql = new StringBuilder()
                .append("select ")
                .append(bean.getSearchColumnsString())
                .append(" from ")
                .append(bean.getTableName());
        return sql;
    }

    /**
     * 按条件查询多个
     *
     * @param ms
     * @param criteria
     * @return
     */
    public String findMore(MappedStatement ms, Criteria criteria) {
        Bean bean = EntityAssistant.getBean(ms);
        StringBuilder sql = searchSegment(bean);
        sql.append(criteria.getWhere());
        sql.append(criteria.getOrderBy());
        sql.append(criteria.getLimit());
        debug(sql);
        return sql.toString();
    }

    /**
     * 按条件计算
     *
     * @param ms
     * @param criteria
     * @return
     */
    public String countMore(MappedStatement ms, Criteria criteria) {
        Bean bean = EntityAssistant.getBean(ms);
        StringBuilder sql = new StringBuilder("select count(1) from ");
        sql.append(bean.getTableName());
        sql.append(criteria.getWhere());
        debug(sql);
        return sql.toString();
    }


    /**
     * 单个插入
     *
     * @param ms
     * @param obj
     * @return
     */
    public String insert(MappedStatement ms, Object obj) {
        Bean bean = EntityAssistant.getBean(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ")
                .append(bean.getTableName())
                .append(" (");
        List<cn.xiaoyaoji.source.mapper.Column> columns = null;
        if (EntityAssistant.getCurdConfig().isInsertSelective()) {
            columns = EntityAssistant.getNonEmptyField(obj);
        } else {
            columns = bean.getColumns();
        }
        Set<String> insertableNames = bean.getInsertableNames();
        for (cn.xiaoyaoji.source.mapper.Column item : columns) {
            if (!item.isInsertable() || !insertableNames.contains(item.getName())) {
                continue;
            }
            sql.append(item.getName()).append(',');
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") values (");
        for (cn.xiaoyaoji.source.mapper.Column item : columns) {
            if (!insertableNames.contains(item.getName())) {
                continue;
            }
            sql.append("#{");
            sql.append(item.getJavaName()).append("}").append(',');
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        debug(sql);
        return sql.toString();
    }


    public String insertUseGeneratedKeys(MappedStatement ms, Object obj) {
        Bean bean = EntityAssistant.getBean(ms);
        assertNotNull(bean.getPrimaryKeyColumnName(), "primary key is null");
        //动态设置key columns and properties
        //ms.keyColumns
        setFieldValue(ms, "keyColumns", new String[]{bean.getPrimaryKeyColumnName()});
        //ms.keyProperties
        setFieldValue(ms, "keyProperties", new String[]{bean.getPrimaryKeyJavaName()});
        return insert(ms, obj);
    }


    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            Class<?> clazz = obj.getClass();
            Field field = null;
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                field = clazz.getField(fieldName);
            }

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(obj, value);
        } catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException e) {
            throw new CurdException(e);
        }
    }


    /**
     * 插入多个
     *
     * @param ms
     * @param objs
     * @return
     */
    public String inserts(MappedStatement ms, Collection<Object> objs) {
        throw new UnsupportedOperationException("暂时不支持");
    }


    /**
     * 更新非空数据
     *
     * @param ms
     * @param obj
     * @return
     */
    public String updateSelective(MappedStatement ms, Object obj) {
        Bean bean = EntityAssistant.getBean(ms);
        assertNotNull(bean.getPrimaryKeyColumnName(), "primary key is null");
        StringBuilder sql = new StringBuilder("update ")
                .append(bean.getTableName())
                .append(" set ");
        List<cn.xiaoyaoji.source.mapper.Column> nonEmptyColumns = EntityAssistant.getNonEmptyField(obj);
        Set<String> updatableColumns = bean.getUpdatableNames();
        for (cn.xiaoyaoji.source.mapper.Column item : nonEmptyColumns) {
            if (!item.isUpdatable() || !updatableColumns.contains(item.getName())) {
                continue;
            }
            if (bean.getPrimaryKeyJavaName().equals(item.getJavaName())) {
                continue;
            }
            sql.append(item.getName()).append("=").append("#{").append(item.getJavaName()).append("},");
        }

        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where ").append(bean.getPrimaryKeyColumnName())
                .append("=#{")
                .append(bean.getPrimaryKeyJavaName())
                .append("}");
        debug(sql);
        return sql.toString();

    }


    /**
     * 根据id更新所有可更新数据
     *
     * @param ms
     * @param obj
     * @return
     */
    public String update(MappedStatement ms, Object obj) {
        Bean bean = EntityAssistant.getBean(ms);
        assertNotNull(bean.getPrimaryKeyColumnName(), "primary key is null");
        StringBuilder sql = new StringBuilder("update ")
                .append(bean.getTableName())
                .append(" set ");
        for (cn.xiaoyaoji.source.mapper.Column column : bean.getUpdatableColumns()) {
            sql.append(column.getName()).append("=#{")
                    .append(column.getJavaName())
                    .append("},");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where ").append(bean.getPrimaryKeyColumnName()).append("=#{").append(bean.getPrimaryKeyJavaName()).append("}");
        debug(sql);
        return sql.toString();
    }


    /**
     * 根据主键删除
     *
     * @param ms
     * @param obj
     * @return
     */
    public String deleteByPrimaryKey(MappedStatement ms, Object obj) {
        Bean bean = EntityAssistant.getBean(ms);
        assertNotNull(bean.getPrimaryKeyColumnName(), "primary key is null");
        StringBuilder sql = new StringBuilder("delete from ")
                .append(bean.getTableName())
                .append(" where ")
                .append(bean.getPrimaryKeyColumnName())
                .append("=#{")
                .append(bean.getPrimaryKeyJavaName())
                .append("}");
        debug(sql);
        return sql.toString();
    }


    /**
     * 根据非空字段删除
     *
     * @param ms
     * @param obj
     * @return
     */
    public String delete(MappedStatement ms, Object obj) {
        Bean bean = EntityAssistant.getBean(ms);
        StringBuilder sql = new StringBuilder("delete from ")
                .append(bean.getTableName())
                .append(" where ");
        Class<?> clazz = obj.getClass();
        try {
            for (Field field : EntityAssistant.mergeArray(clazz.getDeclaredFields(), clazz.getFields())) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Object value = field.get(obj);
                if (value != null) {
                    String columnName = EntityAssistant.getRawColumnName(field.getAnnotation(Column.class), field.getName());
                    sql.append(columnName).append("=");
                    sql.append("#{")
                            .append(field.getName())
                            .append("} and ");
                }
            }
            sql = sql.delete(sql.length() - 4, sql.length());
            debug(sql);
            return sql.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据主键删除
     *
     * @param ms
     * @param keys
     */
    public String deleteByPrimaryKeys(MappedStatement ms, List keys) {
        int length = keys.size();
        Bean bean = EntityAssistant.getBean(ms);
        assertNotNull(bean.getPrimaryKeyColumnName(), "primary key is null");
        StringBuilder sql = new StringBuilder("delete from ")
                .append(bean.getTableName())
                .append(" where ")
                .append(bean.getPrimaryKeyColumnName())
                .append("in (");
        for (int i = 0; i < length; i++) {
            sql.append("#{").append(bean.getPrimaryKeyJavaName()).append("},");
        }
        sql = sql.delete(sql.length() - 1, sql.length());
        debug(sql);
        return sql.toString();
    }


    private void assertNotNull(Object data, String errorMsg) {
        if (data == null) {
            throw new CurdException(errorMsg);
        }
    }

    private void debug(StringBuilder message) {
        debug(message.toString());
    }

    private void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}
