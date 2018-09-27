package cn.xiaoyaoji.service.biz.project.service;

import cn.xiaoyaoji.service.biz.doc.bean.Doc;
import cn.xiaoyaoji.service.biz.doc.event.DocUpdatedEvent;
import cn.xiaoyaoji.service.biz.doc.service.DocService;
import cn.xiaoyaoji.service.biz.project.bean.Project;
import cn.xiaoyaoji.service.biz.project.bean.ProjectGlobal;
import cn.xiaoyaoji.service.biz.project.event.ProjectCreatedEvent;
import cn.xiaoyaoji.service.biz.project.event.ProjectImportedEvent;
import cn.xiaoyaoji.service.biz.project.mapper.ProjectMapper;
import cn.xiaoyaoji.service.common.AbstractCurdService;
import cn.xiaoyaoji.service.util.AssertUtils;
import cn.xiaoyaoji.service.util.StringUtils;
import cn.xiaoyaoji.source.mapper.CurdMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: zhoujingjie
 * @Date: 17/4/16
 */
@Service
public class ProjectService implements AbstractCurdService<Project> {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private DocService docService;
    @Autowired
    private ProjectGlobalService projectGlobalService;


    /**
     * 修改文件修改时间
     */
    @org.springframework.context.event.EventListener(classes = DocUpdatedEvent.class)
    protected void listenDocUpdatedEvent(DocUpdatedEvent event) {
        String projectId = event.getSource().getProjectId();
        Project project = new Project();
        project.setId(projectId);
        project.setLastUpdateTime(new Date());
        projectMapper.updateSelective(project);
    }


    @Override
    public int insertUseGeneratedKeys(Project bean) {
        return insert(bean);
    }

    @Override
    public int insert(Project bean) {
        int rs = projectMapper.insert(bean);
        applicationEventPublisher.publishEvent(new ProjectCreatedEvent(bean));
        return rs;
    }

    /**
     * 查询所有有效的项目id
     */
    public List<String> getAllValidIds() {
        return projectMapper.selectAllValidIds();
    }

    public boolean checkProjectIsPublic(String projectId) {
        return projectMapper.countWithPermission(projectId) > 0;
    }


    @Override
    public int deleteByPrimaryKey(Object key) {
        int rs = projectMapper.deleteByPrimaryKey(key);
        //todo 发布项目已删除事件
        //删除文档
        //删除文档历史
        //删除项目与文档关联
        //删除分享
        return rs;
    }

    public List<Project> getProjectsByUserId(String userId, int status) {
        return projectMapper.selectByUserId(userId, status);
    }

    @Override
    public CurdMapper<Project> getMapper() {
        return projectMapper;
    }

    /**
     * 导入项目
     *
     * @param project 项目
     * @param userId  用户id
     * @return 包含id的项目对象
     */
    public Project importProject(Project project, String userId) {
        //暂时不支持导入文档历史
        String projectId = StringUtils.id();
        project.setId(projectId);
        project.setUserId(userId);
        project.setStatus(1);
        project.setPermission(0);
        projectMapper.insert(project);
        applicationEventPublisher.publishEvent(new ProjectImportedEvent(project));
        return project;
    }
}

