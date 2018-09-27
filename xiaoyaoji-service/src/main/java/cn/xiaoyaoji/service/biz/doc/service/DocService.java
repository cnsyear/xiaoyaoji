package cn.xiaoyaoji.service.biz.doc.service;

import cn.xiaoyaoji.service.Message;
import cn.xiaoyaoji.service.biz.doc.bean.Doc;
import cn.xiaoyaoji.service.biz.doc.mapper.DocMapper;
import cn.xiaoyaoji.service.biz.project.bean.Project;
import cn.xiaoyaoji.service.biz.project.event.ProjectCreatedEvent;
import cn.xiaoyaoji.service.biz.project.event.ProjectImportedEvent;
import cn.xiaoyaoji.service.common.AbstractCurdService;
import cn.xiaoyaoji.service.biz.doc.view.DocType;
import cn.xiaoyaoji.service.util.AssertUtils;
import cn.xiaoyaoji.service.util.StringUtils;
import cn.xiaoyaoji.source.mapper.CurdMapper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: zhoujingjie
 * @Date: 17/4/16
 */
@Service
public class DocService implements AbstractCurdService<Doc> {
    private static Logger logger = LoggerFactory.getLogger(DocService.class);
    @Autowired
    private DocMapper docMapper;

    @Autowired
    private DocHistoryService docHistoryService;


    @Override
    public CurdMapper<Doc> getMapper() {
        return docMapper;
    }


    /**
     * 项目已创建事件
     * @param event
     */
    @org.springframework.context.event.EventListener(classes = ProjectCreatedEvent.class)
    protected void listenProjectCreatedEvent(ProjectCreatedEvent event) {
        createDefaultDoc(event.getProject().getId());
    }

    /**
     * 项目已导入事件
     * @param event
     */
    @org.springframework.context.event.EventListener(classes = ProjectImportedEvent.class)
    protected void listenProjectImportedEvent(ProjectImportedEvent event) {
        Project project = event.getProject();
        if (project.getDocs().isEmpty()) {
            createDefaultDoc(project.getId());
        } else {
            //以下代码应该解耦
            //导入文档
            project.getDocs().forEach(item -> {
                importDoc(item, project.getId(), "0");
            });
        }
    }


    /**
     * 创建文档
     *
     * @param doc
     * @return
     */
    public int createDoc(Doc doc) {
        return docMapper.insert(doc);
    }

    // 创建默认分类
    public Doc createDefaultDoc(String projectId) {
        Doc doc = new Doc();
        doc.setId(StringUtils.id());
        doc.setName("默认文档");
        doc.setCreateTime(new Date());
        doc.setSort(0);
        doc.setProjectId(projectId);
        doc.setLastUpdateTime(new Date());
        doc.setParentId("0");
        doc.setType(DocType.SYS_DOC_MD.getTypeName());
        int rs = docMapper.insert(doc);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return doc;
    }

    /**
     * 导入文档
     *
     * @param doc       文档对象
     * @param projectId 项目id
     * @param parentId  父级id
     * @return 包含id的文档对象
     */
    public Doc importDoc(Doc doc, String projectId, String parentId) {
        doc.setProjectId(projectId);
        doc.setParentId(parentId);
        if (Strings.isNullOrEmpty(doc.getType())) {
            doc.setType(DocType.SYS_DOC_MD.getTypeName());
        }
        doc.setId(StringUtils.id());
        createDoc(doc);
        doc.getChildren().forEach(item -> {
            importDoc(item, projectId, doc.getId());
        });
        return doc;
    }

    public List<Doc> searchDocs(String projectId, String text) {
        return docMapper.selectDoc(projectId, text);
    }


    /**
     * 修改排序
     *
     * @param idsorts id-sort
     * @return rows
     */
    public int updateSort(String[] idsorts) {
        int rs = 0;
        for (String is : idsorts) {
            String[] temp = is.split("_");
            if (temp.length == 2) {
                String id = temp[1], sort = temp[0];
                Doc doc = new Doc();
                doc.setId(id);
                doc.setSort(Integer.valueOf(sort));
                rs += docMapper.updateSelective(doc);
            }
        }
        return rs;
    }

    public int deleteDoc(String id) {
        //需要优化
        Doc doc = docMapper.findOne(id);
        if (doc == null) {
            return 0;
        }
        Set<String> ids = new HashSet<>();
        getDocIdsByParentId(ids, id, doc.getProjectId());
        ids.add(id);
        //删除数据
        int rs = deleteByIds(new ArrayList<>(ids));
        //todo 发布文档已删除事件
       /* ServiceFactory serviceFactory = ServiceFactory.instance();
        for (String temp : ids) {
            //删除附件
            List<DocAttachment> docAttachments = serviceFactory.getAttachsByRelatedId(temp);
            for (DocAttachment docAttachment : docAttachments) {
                try {
                    FileManager.getFileProvider().delete(docAttachment.getUrl());
                    serviceFactory.delete(TableNames.ATTACH, docAttachment.getId());
                    rs++;
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            //删除历史记录
            DataFactory.instance().deleteDocHistoryByDocId(temp);
        }*/
        return rs;
    }


    public void getDocIdsByParentId(Set<String> ids, String parentId, String projectId) {
        List<Doc> temp = docMapper.selectByParentId(projectId, parentId);
        for (Doc item : temp) {
            ids.add(item.getId());
            getDocIdsByParentId(ids, item.getId(), projectId);
        }
    }

    private int deleteByIds(List<String> ids) {
        return docMapper.deleteByPrimaryKeys(ids);
    }

    public List<Doc> getByParentId(String projectId, String parentId) {
        return docMapper.selectByParentId(projectId, parentId);
    }

    public String getConcatNameFromIds(List<String> docIds) {
        if (docIds == null || docIds.isEmpty()) {
            return "";
        }
        return docMapper.selectNameFromIds(docIds);
    }

    /**
     * 复制文档
     *
     * @param docId
     * @param toProjectId 复制到某个项目.如果为空表示当前项目
     * @return
     */
    public int copyDoc(final String docId, String toProjectId) {
        String newDocId = StringUtils.id();
        //如果是复制到其他项目，则直接复制到根目录
        String parentId = toProjectId == null ? null : "0";
        int rs = copyDoc0(docId, newDocId, parentId, toProjectId);
        rs += copyDocs(docId, newDocId, toProjectId);
        return rs;
    }

    /**
     * 复制文档
     *
     * @param docId    原文档id
     * @param newDocId 新文档id
     * @param parentId 父级ID
     * @return rs
     */
    private int copyDoc0(String docId, String newDocId, String parentId, String projectId) {
        Doc doc = docMapper.findOne(docId);
        if (doc == null) {
            return 0;
        }
        doc.setCreateTime(new Date());
        doc.setLastUpdateTime(new Date());
        doc.setId(newDocId);
        if (projectId != null) {
            doc.setProjectId(projectId);
        }
        if (parentId != null) {
            doc.setParentId(parentId);
        }
        return docMapper.insert(doc);
    }

    /**
     * 复制
     *
     * @param parentDocId
     * @param newParentDocId
     * @param projectId      项目id
     * @return
     */
    private int copyDocs(String parentDocId, String newParentDocId, String projectId) {
        //根据父级id查询自节点
        List<Doc> docs = docMapper.selectByParentId(projectId, parentDocId);
        int rs = 0;
        if (docs != null && docs.size() > 0) {
            for (Doc item : docs) {
                String newDocId = StringUtils.id();
                rs += copyDoc0(item.getId(), newDocId, newParentDocId, projectId);
                rs += copyDocs(item.getId(), newDocId, projectId);
            }
        }
        return rs;
    }

    public List<Doc> getByProjectId(final String projectId, boolean searchContent) {
        return treeDocs(docMapper.selectDocsByProjectId(projectId, searchContent));
    }

    /**
     * 查询树形项目文档
     *
     * @param projectId 项目id
     * @return 树形结构文档
     */
    public List<Doc> getByProjectId(final String projectId) {
        return getByProjectId(projectId, false);
    }


    //转成树形结构
    private List<Doc> treeDocs(List<Doc> docs) {
        Map<String, List<Doc>> docMap = new LinkedHashMap<>();
        //root
        docMap.put("0", new ArrayList<Doc>());
        //
        for (Doc doc : docs) {
            docMap.put(doc.getId(), doc.getChildren());
        }
        for (Doc doc : docs) {
            List<Doc> temp = docMap.get(doc.getParentId());
            if (temp != null) {
                temp.add(doc);
            }
        }
        return docMap.get("0");
    }

    public List<String> getRatherThanNumsDocIds(int num) {
        return docMapper.selectRatherThanNumsDocIds(num);
    }

    public List<Doc> getProjectDocs(String projectId) {
        return docMapper.selectDocsByProjectId(projectId, false);
    }
}
