package cn.com.xiaoyaoji.doc.service;

import cn.com.xiaoyaoji.core.common.DocType;
import cn.com.xiaoyaoji.data.bean.Doc;
import cn.com.xiaoyaoji.data.bean.DocHistory;
import cn.com.xiaoyaoji.data.bean.User;
import cn.com.xiaoyaoji.doc.event.DocUpdatedEvent;
import cn.com.xiaoyaoji.service.ProjectService;
import cn.com.xiaoyaoji.service.ServiceFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

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
 * <p>
 * 文档历史记录
 *
 * @author: zhoujingjie
 * Date: 2018/9/8
 */
@Service
public class DocHistoryService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 监听文档改变事件
     *
     * @param event
     */
    @EventListener(classes = DocUpdatedEvent.class)
    @Async
    protected void listenDocUpdatedEvent(DocUpdatedEvent event) {
        String comment = event.getRemark();
        Doc source = event.getSource();
        Doc now = event.getNow();
        User user = event.getUser();

        if (org.apache.commons.lang3.StringUtils.isBlank(comment)) {
            comment = compareDocUpdateRecord(source, now);
            if (comment.length() == 0) {
                comment = "修改文档";
            }
        }
        DocHistory history = new DocHistory();
        BeanUtils.copyProperties(source, history);
        history.setName(now.getName());
        history.setComment(comment);
        history.setUserId(user.getId());
        history.setCreateTime(new Date());
        history.setDocId(source.getId());
        ServiceFactory.instance().create(history);
        //修改最后更新时间
        ProjectService.instance().updateLastUpdateTime(source.getProjectId());
    }

    private String compareDocUpdateRecord(Doc old, Doc now) {
        StringBuilder sb = new StringBuilder();
        if (compareModify(old.getName(), now.getName())) {
            sb.append("文档名称,");
        }
        if (DocType.SYS_HTTP.getTypeName().equals(old.getType())) {
            if (old.getContent() == null) {
                if (now.getContent() != null)
                    sb.append("文档内容,");
            } else if (now.getContent() != null) {

                JSONObject oldObj = JSON.parseObject(old.getContent());
                JSONObject newObj = JSON.parseObject(now.getContent());
                if (compareModify(oldObj.getString("requestMethod"), newObj.getString("requestMethod"))) {
                    sb.append("请求方法,");
                }
                if (compareModify(oldObj.getString("dataType"), newObj.getString("dataType"))) {
                    sb.append("数据类型,");
                }
                if (compareModify(oldObj.getString("contentType"), newObj.getString("contentType"))) {
                    sb.append("响应类型,");
                }
                if (compareModify(oldObj.getString("status"), newObj.getString("status"))) {
                    sb.append("状态,");
                }
                if (compareModify(oldObj.getString("ignoreGHttpReqArgs"), newObj.getString("ignoreGHttpReqArgs"))) {
                    sb.append("忽略全局请求参数,");
                }
                if (compareModify(oldObj.getString("ignoreGHttpReqHeaders"), newObj.getString("ignoreGHttpReqHeaders"))) {
                    sb.append("忽略全局请求头,");
                }
                if (compareModify(oldObj.getString("ignoreGHttpRespHeaders"), newObj.getString("ignoreGHttpRespHeaders"))) {
                    sb.append("忽略全局响应头,");
                }
                if (compareModify(oldObj.getString("ignoreGHttpRespArgs"), newObj.getString("ignoreGHttpRespArgs"))) {
                    sb.append("忽略全局响应参数,");
                }
                if (compareModify(oldObj.getString("description"), newObj.getString("description"))) {
                    sb.append("接口描述,");
                }
                if (compareModify(oldObj.getString("requestArgs"), newObj.getString("requestArgs"))) {
                    sb.append("请求参数,");
                }
                if (compareModify(oldObj.getString("requestHeaders"), newObj.getString("requestHeaders"))) {
                    sb.append("请求头,");
                }
                if (compareModify(oldObj.getString("responseHeaders"), newObj.getString("responseHeaders"))) {
                    sb.append("响应头,");
                }
                if (compareModify(oldObj.getString("responseArgs"), newObj.getString("responseArgs"))) {
                    sb.append("响应数据,");
                }
                if (compareModify(oldObj.getString("example"), newObj.getString("example"))) {
                    sb.append("示例数据,");
                }
            }
        }
        if (sb.length() > 0) {
            sb = sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    private boolean compareModify(String old, String now) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(now) && !now.equals(old)) {
            return true;
        }
        return false;
    }

}
