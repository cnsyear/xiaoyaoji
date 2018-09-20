package cn.xiaoyaoji.source.mapper;

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
 * 配置类
 *
 * @author zhoujingjie
 * Date 2017/11/27
 */
public class MapperConfig {
    /**
     * 驼峰转下划线
     */
    private boolean camelCaseToUnderscore;

    /**
     * 插入非空数据;
     * false:表示插入全部字段的数据。不管是不是null
     */
    private boolean insertSelective = true;

    public boolean isCamelCaseToUnderscore() {
        return camelCaseToUnderscore;
    }

    public MapperConfig setCamelCaseToUnderscore(boolean camelCaseToUnderscore) {
        this.camelCaseToUnderscore = camelCaseToUnderscore;
        return this;
    }

    public boolean isInsertSelective() {
        return insertSelective;
    }

    public MapperConfig setInsertSelective(boolean insertSelective) {
        this.insertSelective = insertSelective;
        return this;
    }

    public static MapperConfig build() {
        return new MapperConfig();
    }
}
