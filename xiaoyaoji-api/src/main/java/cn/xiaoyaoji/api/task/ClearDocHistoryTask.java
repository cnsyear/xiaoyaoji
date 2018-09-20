package cn.xiaoyaoji.api.task;

import cn.xiaoyaoji.service.biz.doc.service.DocHistoryService;
import cn.xiaoyaoji.service.biz.doc.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
 * Date: 2018/9/4
 */
@Component
public class ClearDocHistoryTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${xyj.task.clear-doc-history-interval:86400}")
    private int taskClearDocHistoryInterval;

    @Autowired
    private DocService docService;
    @Autowired
    private DocHistoryService docHistoryService;

    @EventListener()
    @Async
    public void run(ApplicationStartedEvent event) {
        //第一次10分钟后执行
        int initialDelay = 10 * 60;
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, (r -> new Thread(r, "clear_doc_history_thread")));
        executor.scheduleWithFixedDelay(() -> {
            //删除文档历史记录。 每个文档只保留15份
            //查询文档记录数量查过15条的文档id
            logger.info("执行清除docHistory计划");
            List<String> docIds = docService.getRatherThanNumsDocIds(15);
            for (String docId : docIds) {
                docHistoryService.deleteDocHistoryThanLimit(15, docId);
                logger.info("清除{}", docId);
            }
        }, initialDelay, taskClearDocHistoryInterval, TimeUnit.SECONDS);

    }
}
