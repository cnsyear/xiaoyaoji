package cn.xiaoyaoji.api.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.Calendar;
import java.util.Date;

/**
 * 生成sitemap
 *
 * @author zhoujingjie
 *         created on 2017/8/29
 */
@Component
@ConditionalOnProperty(name = "xyj.sitemap.enable", havingValue = "true")
public class SiteMapTask implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(SiteMapTask.class);
    private ServletContext servletContext;

    private static SiteMapTask instance;


    private SiteMapTask(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    //手动运行
    public void manualRunTask() {
        instance.run();
    }


    @EventListener
    @Async
    public void run(ApplicationStartedEvent event) {

    }

    public static void start(ServletContext servletContext) {
        if (instance == null) {
            instance = new SiteMapTask(servletContext);
        }
        //每天凌晨1点执行
        Date oclock1 = getClock(1);
        //ScheduleManager.schedule(instance, oclock1, 24 * 60 * 60 * 1000);

    }

    private static Date getClock(int hour) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) > hour) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private String getURL(String id) {
        return "http://www.xiaoyaoji.cn/project/" + id;
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        //
        //查询所有有效的projectId
       /* List<String> ids = ServiceFactory.instance().getAllProjectValidIds();
        if (ids.size() == 0)
            return;
        logger.info("the daily sitemap generation task begin");
        File siteMapFile = new File(servletContext.getRealPath("/sitemap.txt"));
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(siteMapFile), StandardCharsets.UTF_8);
            writer.write("http://www.xiaoyaoji.cn/");
            for (String id : ids) {
                writer.write(getURL(id));
                writer.write("\n");
            }
            logger.info("the daily sitemap generation task has completed");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(writer);
        }*/
    }
}
