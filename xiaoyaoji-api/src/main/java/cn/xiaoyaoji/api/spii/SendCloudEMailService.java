package cn.xiaoyaoji.api.spii;

import cn.xiaoyaoji.service.AppCts;
import cn.xiaoyaoji.service.common.HashMapX;
import cn.xiaoyaoji.service.spi.EmailService;
import com.alibaba.fastjson.JSON;
import jodd.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * //todo 该功能做成插件
 *
 * @author zhoujingjie
 *         created on 2017/5/18
 */
public class SendCloudEMailService implements EmailService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static String TEMPLATE_URL = "http://sendcloud.sohu.com/webapi/mail.send_template.json";

    @Value("${sendcloud.api-key}")
    private String apiKey;

    @Value("${sendcloud.api-user}")
    private String apiUser;

    /**
     * 发送人地址
     */
    @Value("${sendcloud.from}")
    private String from;

    /**
     * 发送人名称
     */
    @Value("${sendcloud.from-name:小幺鸡系统通知}")
    private String fromName;

    /**
     * 验证码模板名称
     */
    @Value("${sendcloud.template.captcha-name:captcha}")
    private String captchaTemplateName;

    /**
     * 验证码模板名称
     */
    @Value("${sendcloud.template.findpassword-name:find_password}")
    private String findPasswordTemplateName;


    /**
     * 发送验证码
     *
     * @param code 验证码
     * @param to   收件人
     */
    @Override
    public void sendCaptcha(String code, String to) {
        String vars = JSON.toJSONString(new HashMapX<>().append("to", new String[]{to}).append("sub", new HashMapX<>().append("%captcha%", new String[]{code})));
        String body = new String(
                HttpRequest.post(TEMPLATE_URL).form(new HashMapX<String, Object>()
                        .append("api_user", apiUser)
                        .append("api_key", apiKey)
                        .append("from", from)
                        .append("fromname", fromName)
                        .append("subject", "小幺鸡系统通知-验证码")
                        .append("substitution_vars", vars)
                        .append("use_maillist", "false")
                        .append("template_invoke_name", captchaTemplateName)
                )
                        .send().bodyBytes(), AppCts.UTF8
        );
        if (body.contains("error")) {
            throw new RuntimeException(body);
        }
        logger.debug(body);
    }

    /**
     * 发送找回密码
     *
     * @param findPageURL 找回密码链接地址
     * @param to          收件人
     */
    @Override
    public void sendFindPassword(String findPageURL, String to) {
        String vars = JSON.toJSONString(new HashMapX<>()
                .append("to", new String[]{to}).append("sub", new HashMapX<>().append("%url%", new String[]{findPageURL})));
        String body = new String(HttpRequest.post(TEMPLATE_URL).
                form(new HashMapX<String, Object>().append("api_user", apiKey)
                        .append("api_user", apiUser)
                        .append("from", from)
                        .append("fromname", fromName)
                        .append("subject", "小幺鸡系统通知-找回密码")
                        .append("substitution_vars", vars)
                        //.append("use_maillist", "false")
                        .append("template_invoke_name", findPasswordTemplateName)
                )
                .send()
                .bodyBytes(), AppCts.UTF8);
        if (body.contains("error")) {
            throw new RuntimeException(body);
        }
        logger.debug(body);
    }
}
