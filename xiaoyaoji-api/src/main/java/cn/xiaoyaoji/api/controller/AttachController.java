package cn.xiaoyaoji.api.controller;

import cn.xiaoyaoji.api.extension.file.FileUtils;
import cn.xiaoyaoji.api.extension.file.MetaData;
import cn.xiaoyaoji.service.annotations.Ignore;
import cn.xiaoyaoji.service.biz.doc.bean.DocAttachment;
import cn.xiaoyaoji.service.biz.doc.service.DocAttachmentService;
import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.service.common.HashMapX;
import cn.xiaoyaoji.service.common.Result;
import cn.xiaoyaoji.service.util.AssertUtils;
import cn.xiaoyaoji.service.util.ConfigUtils;
import cn.xiaoyaoji.service.util.StringUtils;
import com.google.common.html.HtmlEscapers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author: zhoujingjie
 * @Date: 17/5/2
 */
@RestController
@RequestMapping("/attach")
public class AttachController {
    @Value("#{${xyj.upload.excludes:}.split(',')}")
    private Set<String> excludes;
    @Autowired
    private DocAttachmentService docAttachmentService;

    @PostMapping
    public Object upload(@RequestParam("file") List<MultipartFile> files, @RequestParam String relateId, @RequestParam String projectId) throws IOException {
        AssertUtils.notNull(relateId, "missing relatedId");
        AssertUtils.isTrue(files != null && files.size() > 0, "请上传文件");
        for (MultipartFile file : files) {
            String contentType = file.getContentType();
            //判断类型
            for (String exclude : excludes) {
                if (exclude.matches(contentType)) {
                    return new Result<>(false, "不允许上传该文件类型:" + exclude);
                }
            }
        }
        for (MultipartFile file : files) {
            MetaData md = FileUtils.upload(file);
            String path = md.getPath();
            DocAttachment temp = new DocAttachment();
            temp.setId(StringUtils.id());
            temp.setUrl(path);
            temp.setType(md.getType().name());
            temp.setSort(10);
            temp.setCreateTime(new Date());
            temp.setRelatedId(relateId);
            temp.setFileName(HtmlEscapers.htmlEscaper().escape(file.getOriginalFilename()));
            temp.setProjectId(projectId);
            docAttachmentService.insert(temp);
        }
        return true;
    }

    @Ignore
    @GetMapping("/{relatedId}")
    public Object get(@PathVariable String relatedId, @RequestParam String projectId, User user) {
        //ServiceTool.checkUserHasAccessPermission(projectId,user);
        List<DocAttachment> docAttachments = docAttachmentService.getAttachsByRelatedId(relatedId);
        return new HashMapX<>()
                .append("fileAccess", ConfigUtils.getFileAccessURL())
                .append("attachs", docAttachments);
    }


    /**
     * 删除附件
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable String id) {
        //权限检查
        DocAttachment docAttachment = docAttachmentService.findOne(id);
        AssertUtils.isTrue(docAttachment != null, "无效ID");
        int rs = docAttachmentService.deleteByPrimaryKey(id);
        try {
            FileUtils.delete(docAttachment.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
