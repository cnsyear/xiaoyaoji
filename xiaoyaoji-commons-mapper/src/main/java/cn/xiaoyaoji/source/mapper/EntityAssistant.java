package cn.xiaoyaoji.source.mapper;

import org.apache.ibatis.mapping.MappedStatement;

import javax.persistence.Column;
import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
 * bean工具类
 *
 * @author zhoujingjie
 *         Date 2017/11/23
 */
public class EntityAssistant {
    private static Map<String, Bean> entityMap = new ConcurrentHashMap<>();

    private static MapperConfig config = new MapperConfig();

    static void setCurdConfig(MapperConfig mapperConfig) {
        EntityAssistant.config = mapperConfig;
    }

    public static MapperConfig getCurdConfig() {
        return config;
    }


    public static String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table != null) {
            return table.name();
        }
        StringBuilder tableName = new StringBuilder();
        int index = 0;
        for (char c : clazz.getSimpleName().toCharArray()) {
            if (Character.isUpperCase(c)) {
                if (index++ != 0) {
                    tableName.append('_');
                }
                tableName.append(Character.toLowerCase(c));
            } else {
                tableName.append(c);
            }
        }
        return tableName.toString();
    }

    public static Class<?> getMapperClass(MappedStatement ms) {
        String msId = ms.getId();
        String className = msId.substring(0, msId.lastIndexOf("."));
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getException());
        }
    }

    /**
     * 获取请求bean
     *
     * @param ms
     * @return
     */
    public static Bean getBean(MappedStatement ms) {
        String msId = ms.getId();
        Bean clazz = entityMap.get(msId);
        if (clazz != null) {
            return clazz;
        }
        synchronized (entityMap) {
            clazz = entityMap.get(msId);
            if (clazz != null) {
                return clazz;
            }
        }
        Class<?> mapperClass = getMapperClass(ms);
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
                Class<?> returnType = (Class<?>) t.getActualTypeArguments()[0];
                //获取该类型后，第一次对该类型进行初始化
                Bean bean = new Bean();
                initializeColumns(returnType, bean);
                bean.setTableName(getTableName(returnType));
                entityMap.put(ms.getId(), bean);
                return bean;
            }
        }
        throw new RuntimeException("无法获取 " + msId + " 方法的泛型信息!");
    }

    /**
     * 初始化列
     *
     * @param clazz
     * @param bean
     */
    private static void initializeColumns(Class clazz, Bean bean) {
        Set<Field> fields = new LinkedHashSet<>(Arrays.asList(clazz.getFields()));
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : fields) {
            if (field.getAnnotation(Transient.class) != null) {
                continue;
            }
            if (field.getAnnotation(JoinColumn.class) != null) {
                continue;
            }
            if (field.getAnnotation(JoinColumns.class) != null) {
                continue;
            }
            String columnName = null;
            boolean insertable = true, updatable = true;
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                insertable = column.insertable();
                updatable = column.updatable();
            }
            columnName = getRawColumnName(column, field.getName());
            bean.getColumns().add(new cn.xiaoyaoji.source.mapper.Column(columnName, field.getName(), insertable, updatable));
            //设置主键
            if (field.getAnnotation(Id.class) != null) {
                bean.setPrimaryKeyColumnName(columnName);
                bean.setPrimaryKeyJavaName(field.getName());
            }
        }
    }

    /**
     * 获取数据库原始列名
     *
     * @param column
     * @param javaFieldName
     * @return
     */
    public static String getRawColumnName(Column column, String javaFieldName) {
        if (column != null && column.name().length() > 0) {
            return column.name();
        }
        if (config.isCamelCaseToUnderscore()) {
            StringBuilder columnName = new StringBuilder();
            for (char ch : javaFieldName.toCharArray()) {
                if (Character.isUpperCase(ch)) {
                    columnName.append('_').append(Character.toLowerCase(ch));
                } else {
                    columnName.append(ch);
                }
            }
            return columnName.toString();
        }
        return javaFieldName;
    }


    /**
     * 获取非空字段
     *
     * @param obj
     * @return
     */
    public static List<cn.xiaoyaoji.source.mapper.Column> getNonEmptyField(Object obj) {
        Class<?> clazz = obj.getClass();
        try {
            List<cn.xiaoyaoji.source.mapper.Column> columns = new ArrayList<>();
            for (Field field : mergeArray(clazz.getDeclaredFields(), clazz.getFields())) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Object value = field.get(obj);
                if (value != null) {
                    Column temp = field.getAnnotation(Column.class);
                    boolean insertable = true, updatable = true;
                    if (temp != null) {
                        insertable = temp.insertable();
                        updatable = temp.updatable();
                    }
                    String columnName = getRawColumnName(temp, field.getName());
                    columns.add(new cn.xiaoyaoji.source.mapper.Column(columnName, field.getName(), insertable, updatable));
                }
            }
            return columns;
        } catch (IllegalAccessException e) {
            throw new CurdException(e);
        }
    }

    /**
     * 合并数组
     *
     * @param arr1
     * @param arr2
     * @param <T>
     * @return
     */
    public static <T> List<T> mergeArray(T[] arr1, T[] arr2) {
        List<T> arr = new ArrayList<>(arr1.length + arr2.length);
        Collections.addAll(arr, arr1);
        Collections.addAll(arr, arr2);
        return arr;
    }

}
