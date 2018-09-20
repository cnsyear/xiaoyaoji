package cn.xiaoyaoji.source.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
 * 操作bean类
 *
 * @author zhoujingjie
 *         Date 2017/11/24
 */
public class Bean {
    /**
     * 主键列名
     */
    private String primaryKeyColumnName = "id";
    /**
     * 主键java名称
     */
    private String primaryKeyJavaName = "id";
    /**
     * 所有列
     */
    private List<Column> columns = new ArrayList<>();
    /**
     * 表名
     */
    private String tableName;

    public String getPrimaryKeyColumnName() {
        return primaryKeyColumnName;
    }

    public void setPrimaryKeyColumnName(String primaryKeyColumnName) {
        this.primaryKeyColumnName = primaryKeyColumnName == null ? null : primaryKeyColumnName.trim();
    }

    public String getPrimaryKeyJavaName() {
        return primaryKeyJavaName;
    }

    public void setPrimaryKeyJavaName(String primaryKeyJavaName) {
        this.primaryKeyJavaName = primaryKeyJavaName == null ? null : primaryKeyJavaName.trim();
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Column> getColumns() {
        return columns;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }


    public String getColumnsString(boolean checkInsertable, boolean checkUpdatable) {
        return getColumns().stream().map(column -> {
            if (checkInsertable || checkUpdatable) {
                if ((checkInsertable && column.isInsertable()) //
                        || (checkUpdatable && column.isUpdatable())) {
                    return column.getName();
                }
                return null;
            } else {
                return column.getName();
            }
        }).filter(item -> item != null)
                .collect(Collectors.joining(","));
    }

    public String getSearchColumnsString() {
        StringBuffer sb = new StringBuffer();
        for (Column column : getColumns()) {
            sb.append(column.getName());
            if (!column.getName().equals(column.getJavaName())) {
                sb.append(" as ").append(column.getJavaName());
            }
            sb.append(",");
        }
        sb = sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }


    public String getColumnsString() {
        return getColumnsString(false, false);
    }

    public String getInsertableColumnsString() {
        return getColumnsString(true, false);
    }

    public String getUpdateColumnsString() {
        return getColumnsString(false, true);
    }

    public List<Column> getInsertableColumns() {
        return getColumns().stream().filter(column -> column.isInsertable()).collect(Collectors.toList());
    }

    public List<Column> getUpdatableColumns() {
        return getColumns().stream().filter(column -> column.isUpdatable()).collect(Collectors.toList());
    }

    public Set<String> getUpdatableNames() {
        return getColumns().stream().filter(column -> column.isUpdatable()).map(column -> column.getName()).collect(Collectors.toSet());
    }

    public Set<String> getInsertableNames() {
        return getColumns().stream().filter(column -> column.isInsertable()).map(column -> column.getName()).collect(Collectors.toSet());
    }

}
