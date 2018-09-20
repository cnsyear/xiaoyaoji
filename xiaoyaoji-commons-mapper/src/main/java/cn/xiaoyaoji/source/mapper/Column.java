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
 * 列
 *
 * @author zhoujingjie
 * Date 2017/11/27
 */
public class Column {
    /**
     * 数据库列名
     */
    private String name;
    /**
     * java名
     */
    private String javaName;
    /**
     * 可新增
     */
    private boolean insertable;
    /**
     * 可修改
     */
    private boolean updatable;

    public Column() {
    }

    public Column(String name, String javaName, boolean insertable, boolean updatable) {
        this.name = name;
        this.javaName = javaName;
        this.insertable = insertable;
        this.updatable = updatable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public boolean isInsertable() {
        return insertable;
    }

    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        if (insertable != column.insertable) return false;
        if (updatable != column.updatable) return false;
        return name != null ? name.equals(column.name) : column.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (insertable ? 1 : 0);
        result = 31 * result + (updatable ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "cn.xiaoyaoji.source.mapper.Column{" +
                "name='" + name + '\'' +
                ", insertable=" + insertable +
                ", updatable=" + updatable +
                '}';
    }

    public String getJavaName() {
        return javaName;
    }

    public void setJavaName(String javaName) {
        this.javaName = javaName == null ? null : javaName.trim();
    }
}
