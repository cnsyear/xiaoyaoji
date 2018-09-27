package cn.xiaoyaoji.service.biz.project.service;

import cn.xiaoyaoji.service.biz.project.bean.Project;
import cn.xiaoyaoji.service.biz.project.bean.ProjectGlobal;
import cn.xiaoyaoji.service.biz.project.event.ProjectCreatedEvent;
import cn.xiaoyaoji.service.biz.project.event.ProjectImportedEvent;
import cn.xiaoyaoji.service.biz.project.mapper.ProjectGlobalMapper;
import cn.xiaoyaoji.service.common.AbstractCurdService;
import cn.xiaoyaoji.service.util.StringUtils;
import cn.xiaoyaoji.source.mapper.CurdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

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
 * Date: 2018/9/18
 */
@Service
public class ProjectGlobalService implements AbstractCurdService<ProjectGlobal> {
    @Autowired
    private ProjectGlobalMapper projectGlobalMapper;

    @Override
    public CurdMapper<ProjectGlobal> getMapper() {
        return projectGlobalMapper;
    }


    /**
     * 监听项目已创建事件
     *
     * @param event
     */
    @EventListener(classes = ProjectCreatedEvent.class)
    protected void listenProjectCreatedEvent(ProjectCreatedEvent event) {
        String projectId = event.getProject().getId();
        createDefaultProjectGlobal(projectId);
    }
    /**
     * 监听项目已创建事件
     *
     * @param event
     */
    @EventListener(classes = ProjectImportedEvent.class)
    protected void listenProjectImportedEvent(ProjectImportedEvent event) {
        Project project= event.getProject();
        //导入全局项目对象
        if(project.getProjectGlobal() != null) {
            project.getProjectGlobal().setProjectId(project.getId());
            insert(project.getProjectGlobal());
        }else{
            createDefaultProjectGlobal(project.getId());
        }
    }


    /**
     * 创建默认的对象
     *
     * @param projectId 项目id
     * @return pg
     */
    public ProjectGlobal createDefaultProjectGlobal(String projectId) {
        ProjectGlobal pg = new ProjectGlobal();
        pg.setId(StringUtils.id());
        pg.setProjectId(projectId);
        pg.setEnvironment("[]");
        pg.setHttp("{}");
        pg.setStatus("[{\"name\":\"有效\",\"value\":\"ENABLE\",\"t\":1493901719144},{\"name\":\"废弃\",\"value\":\"DEPRECATED\",\"t\":1493901728060}]");
        projectGlobalMapper.insert(pg);
        return pg;
    }


    /**
     * 查询全局环境变量
     *
     * @param projectId 项目id
     */
    public String getEnvironmentByProjectId(String projectId) {
        return getColumnByProjectId(projectId, "environment");
    }

    /**
     * 查询全局http
     *
     * @param projectId 项目id
     */
    public String getHttpByProjectId(String projectId) {
        return getColumnByProjectId(projectId, "http");
    }


    /**
     * 查询project global
     *
     * @param projectId 项目id
     */
    public ProjectGlobal getByProjectId(String projectId) {
        ProjectGlobal pg = projectGlobalMapper.selectByProjectId(projectId);
        if (pg == null) {
            return createDefaultProjectGlobal(projectId);
        }
        return pg;
    }


    private String getColumnByProjectId(String projectId, String column) {
        column = projectGlobalMapper.selectColumnByProjectId(column, projectId);
        //有并发风险
        if (column == null) {
            createDefaultProjectGlobal(projectId);
            return null;
        }
        return column;
    }
}
