package cn.xiaoyaoji.source.mapper;

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
 * <p>
 * 查询条件类
 * usage: <br/>
 * cn.xiaoyaoji.source.mapper.Criteria.build().where().eq(...).and(...).criteria()
 *
 * @author zhoujingjie
 * Date 2017/11/23
 */
public class Criteria extends HashMap<String, Object> {
    private Where where = new Where();
    private int index;
    private List<String> conditions = new ArrayList<>();
    private String orderBy="", limit="";

    public static Criteria build() {
        return new Criteria();
    }


    /**
     * @return
     */
    @Deprecated
    public Where where() {
        return where;
    }


    public String getWhere() {
        StringBuilder sql = new StringBuilder();
        if (conditions.size() > 0) {
            sql.append(" where 1=1 ");
            for (String condition : conditions) {
                sql.append(' ').append(condition);
            }
        }
        return sql.toString();
    }

    public Criteria eq(String column, Object value) {
        put(column, value);
        put(String.valueOf(index++), value);
        conditions.add(" and " + column + "=#{" + column + "} ");
        return this;
    }


    public Criteria or(String column, Object value) {
        put(column, value);
        put(String.valueOf(index++), value);
        conditions.add(" or " + column + "=#{" + column + "} ");
        return this;
    }

    public Criteria like(String column, Object value) {
        put(column, value);
        put(String.valueOf(index++), value);
        conditions.add(" and instr(" + column + ",#{" + column + "})>0");
        return this;
    }

    public Criteria orderBy(String column, Sort sort) {
        this.orderBy = " order by " + column + " " + sort.value;
        return this;
    }

    public Criteria limit(int start, int limit) {
        this.limit = " limit " + start + "," + limit;
        return this;
    }

    public Criteria limit(int limit) {
        this.limit = " limit " + limit;
        return this;
    }

    public String getLimit(){
        return limit;
    }

    public String getOrderBy(){
        return orderBy;
    }
    public enum Sort {
        DESC("desc"), ASC("asc");
        String value;

        Sort(String sort) {
            this.value = sort;
        }
    }


    public class Where {


        public Criteria criteria() {
            return Criteria.this;
        }

        public Where eq(String column, Object value) {
            Criteria.this.eq(column, value);
            return this;
        }

        public Where or(String column, Object value) {
            Criteria.this.or(column, value);
            return this;
        }

        public Where like(String column, Object value) {
            Criteria.this.like(column, value);
            return this;
        }
    }
}
