package cn.xiaoyaoji.api.controller.doc;

import cn.xiaoyaoji.api.base.Session;
import cn.xiaoyaoji.service.XyjProperties;
import cn.xiaoyaoji.service.annotations.Ignore;
import cn.xiaoyaoji.service.biz.doc.bean.DocAttachment;
import cn.xiaoyaoji.service.biz.doc.service.DocAttachmentService;
import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.service.common.HashMapX;
import cn.xiaoyaoji.service.common.Result;
import cn.xiaoyaoji.service.common.ResultModel;
import cn.xiaoyaoji.service.spi.FileUploadService;
import cn.xiaoyaoji.service.util.AssertUtils;
import com.google.common.html.HtmlEscapers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: zhoujingjie
 * @Date: 17/5/2
 */
@RestController
@RequestMapping("/doc/attachment")
public class DocAttachmentController {

    @Autowired
    private DocAttachmentService docAttachmentService;
    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private XyjProperties xyjProperties;

    /**
     * 上传文件
     *
     * @param files     文件列表
     * @param relateId  关联id
     * @param projectId 项目id
     */
    @PostMapping
    public Object upload(@RequestParam("file") List<MultipartFile> files, @RequestParam String relateId, @RequestParam String projectId, @Session User user) throws IOException {
        //todo 检查权限
        AssertUtils.notNull(relateId, "missing relatedId");
        AssertUtils.isTrue(files != null && files.size() > 0, "请上传文件");
        List<String> urls = new ArrayList<>();
        files.forEach(item -> {
            AssertUtils.isTrue(item.getSize() < 1 * 1024 * 1024, "文件大小超过1M");
        });
        for (MultipartFile file : files) {
            String path = fileUploadService.upload(file.getInputStream(), file.getContentType());
            DocAttachment temp = new DocAttachment();
            temp.setUrl(path);
            temp.setType(file.getContentType().startsWith("image") ? "IMG" : "FILE");
            temp.setSort(10);
            temp.setRelatedId(relateId);
            temp.setFileName(HtmlEscapers.htmlEscaper().escape(file.getOriginalFilename()));
            temp.setProjectId(projectId);
            docAttachmentService.insert(temp);
            urls.add(temp.getUrl());
        }
        return urls;
    }

    /**
     * 插件关联的数据
     *
     * @param relatedId
     * @param projectId
     */
    @Ignore
    @GetMapping("/{relatedId}")
    public Object get(@PathVariable String relatedId, @RequestParam String projectId, @Session User user) {
        //检查有误权限
        //ServiceTool.checkUserHasAccessPermission(projectId,user);
        List<DocAttachment> docAttachments = docAttachmentService.getAttachsByRelatedId(relatedId);
        return new HashMapX<>()
                .append("fileAccess", xyjProperties.getFileAccess())
                .append("attachments", docAttachments);
    }


    /**
     * 删除附件
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable int id) {
        //todo 权限检查
        DocAttachment docAttachment = docAttachmentService.findOne(id);
        AssertUtils.isTrue(docAttachment != null, "无效ID");
        int rs = docAttachmentService.deleteByPrimaryKey(id);
        fileUploadService.delete(docAttachment.getUrl());
        return true;
    }

    /**
     * 修改排序
     *
     * @param idSorts
     * @return
     */
    @PostMapping("sort")
    public int updateSort(@RequestParam("idSort") String[] idSorts) {
        //todo 权限检查
        for (String idSort : idSorts) {
            String[] temp = idSort.split("_");
            DocAttachment dt = new DocAttachment();
            dt.setId(Integer.parseInt(temp[0]));
            dt.setSort(Integer.parseInt(temp[1]));
            docAttachmentService.updateSelective(dt);
        }
        return 1;
    }
}
