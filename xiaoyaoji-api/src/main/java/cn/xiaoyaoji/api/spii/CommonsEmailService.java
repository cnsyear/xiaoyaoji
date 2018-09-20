package cn.xiaoyaoji.api.spii;

import cn.xiaoyaoji.service.spi.EmailService;
import cn.xiaoyaoji.service.util.ConfigUtils;
import org.apache.commons.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 基于commons email 实现
 *
 * @author zhoujingjie
 *         created on 2017/5/18
 */
@Component
@ConditionalOnProperty(value = "xyj.plugin.email.provider", havingValue = "cn.xiaoyaoji.api.spii.CommonsEmailService")
public class CommonsEmailService implements EmailService {
    private Logger logger = LoggerFactory.getLogger(CommonsEmailService.class);
    @Value("${xyj.plugin.email.smtp.server}")
    private String hostName;
    @Value("${xyj.plugin.email.smtp.port}")
    private int port;
    @Value("${xyj.plugin.email.username}")
    private String username;
    @Value("${xyj.plugin.email.password}")
    private String password;
    @Value("${xyj.plugin.email.from}")
    private String from;


    @Override
    public void sendCaptcha(String code, String to) {
        try {
            Email email = new SimpleEmail();
            authentication(email);
            email.setFrom(from);
            email.setCharset("UTF-8");
            email.setSubject("小幺鸡验证码");
            email.setMsg("验证码是：" + code);
            email.addTo(to);
            email.send();
        } catch (EmailException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void authentication(Email email) {
        email.setHostName(hostName);
        email.setSmtpPort(port);
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(true);
    }

    @Override
    public void sendFindPassword(String findPageURL, String to) {
        try {
            HtmlEmail email = new HtmlEmail();
            authentication(email);
            email.setCharset("UTF-8");
            email.addTo(to);
            email.setFrom(from, "系统管理员");
            email.setSubject("找回密码");
            email.setHtmlMsg("<html><body><a href=\"" + findPageURL + "\">点击找回密码</a></body></html>");
            email.setTextMsg("复制地址到浏览器上打开:" + findPageURL);
            email.send();
        } catch (EmailException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
