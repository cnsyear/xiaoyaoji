package cn.xiaoyaoji.source.mapper;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.PropertyParser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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
 *
 * @author zhoujingjie
 * Date 2017/11/24
 */
public class CurdProviderSqlSource implements SqlSource {
    private ProviderSqlSource providerSqlSource;
    private SqlSourceBuilder sqlSourceParser;
    private MappedStatement ms;
    private static Object builder;
    //调用的方法
    private Method providerMethod;

    public CurdProviderSqlSource(MappedStatement ms,ProviderSqlSource providerSqlSource) {
        this.ms = ms;
        this.providerSqlSource = providerSqlSource;
        try {
            this.sqlSourceParser = (SqlSourceBuilder) getFieldValue("sqlSourceParser");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CurdException(e);
        }
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        SqlSource sqlSource = createSqlSource(parameterObject);
        if (sqlSource != null) {
            BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
            if(parameterObject instanceof Criteria){
                Criteria criteria = (Criteria) parameterObject;
                try {
                    setField(boundSql,"parameterObject",criteria);
                } catch (NoSuchFieldException |IllegalAccessException e) {
                    throw new CurdException(e);
                }
            }
            //boundSql.
            return boundSql;
        }
        return providerSqlSource.getBoundSql(parameterObject);
    }

    private SqlSource createSqlSource(Object parameterObject) {
        try {
            if(providerMethod == null){
                providerMethod = (Method) getFieldValue("providerMethod");
            }
            Class<?>[] parameterTypes = providerMethod.getParameterTypes();
            if (parameterTypes != null) {
                if(parameterTypes.length >= 1) {
                    List<Object> args = new ArrayList<>();
                    for(Class<?> type:parameterTypes) {
                        if(MappedStatement.class.isAssignableFrom(type)) {
                            args.add(ms);
                        }else if(Criteria.class.isAssignableFrom(type)){
                            args.add(parameterObject);
                        }else{
                            args.add(parameterObject);
                        }
                    }
                    if(builder == null){
                        builder = ((Class<?>) getFieldValue("providerType")).newInstance();
                    }
                    String sql = (String) providerMethod.invoke(builder, args.toArray());
                    Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
                    return sqlSourceParser.parse(replacePlaceholder(sql), parameterType, new HashMap<>());
                }
            }
            return null;
        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new CurdException(e);
        }
    }

    private String replacePlaceholder(String sql) {
        return PropertyParser.parse(sql, ms.getConfiguration().getVariables());
    }

    private Object getFieldValue(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return getField(fieldName).get(providerSqlSource);
    }

    private Field getField(String fieldName) throws NoSuchFieldException {
        Field field = providerSqlSource.getClass().getDeclaredField(fieldName);
        if(!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field;
    }

    private void setField(Object instance,String fieldName,Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        if(!field.isAccessible()){
            field.setAccessible(true);
        }
        field.set(instance,value);
    }

}
