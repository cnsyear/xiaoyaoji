package cn.xiaoyaoji.source.mapper;

import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;

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
public class MybatisCommonMapperHelper {

    /**
     * 处理mappedStatement
     *
     * @param configuration
     */
    public MybatisCommonMapperHelper processConfiguration(Configuration configuration) {
        for (Object object : new ArrayList<Object>(configuration.getMappedStatements())) {
            if (object instanceof MappedStatement) {
                MappedStatement ms = (MappedStatement) object;
                if (ms.getSqlSource() instanceof ProviderSqlSource) {
                    setSqlSource(ms);
                }
            }
        }
        return this;
    }

    private void setSqlSource(MappedStatement ms) {
        ProviderSqlSource temp = (ProviderSqlSource) ms.getSqlSource();
        SqlSource sqlSource = new CurdProviderSqlSource(ms, temp);
        MetaObject msObject = SystemMetaObject.forObject(ms);
        msObject.setValue("sqlSource", sqlSource);
    }

    public MybatisCommonMapperHelper setCurdConfig(MapperConfig mapperConfig){
        EntityAssistant.setCurdConfig(mapperConfig);
        return this;
    }

}
