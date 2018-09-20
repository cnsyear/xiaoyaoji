package cn.xiaoyaoji.api.extension.email;

import cn.xiaoyaoji.service.common.HashMapX;
import cn.xiaoyaoji.service.util.ConfigUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * //todo 该功能做成插件
 * @author zhoujingjie
 *         created on 2017/5/18
 */
public class SendCloudEMailProvider implements EmailProvider {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static String TEMPLATE_URL = "http://sendcloud.sohu.com/webapi/mail.send_template.json";

    @Override
    public void sendCaptcha(String code, String to) {
     /*   String vars = JSON.toJSONString(new HashMapX<>().append("to", new String[]{to}).append("sub", new HashMapX<>().append("%captcha%", new String[]{code})));
        NameValuePair[] pairs = new NameValuePair[]{new NameValuePair("api_user", ConfigUtils.getProperty("sendcloud.system.apiuser")),
                new NameValuePair("api_key", ConfigUtils.getProperty("sendcloud.apikey")),
                new NameValuePair("from", ConfigUtils.getProperty("sendcloud.system.from")), new NameValuePair("fromname", "小幺鸡系统通知"),
                new NameValuePair("subject", "小幺鸡系统通知-验证码"), new NameValuePair("substitution_vars", vars), new NameValuePair("use_maillist", "false"),
                new NameValuePair("template_invoke_name", "captcha"),};
        String rs = HttpUtils.post(TEMPLATE_URL, pairs);
        if (rs.contains("error")) {
            throw new RuntimeException(rs);
        }
        logger.debug(rs);*/
    }

    @Override
    public void findPassword(String findPageURL, String to) {
        /*String vars = JSON.toJSONString(new HashMapX<>().append("to", new String[]{to}).append("sub", new HashMapX<>().append("%url%", new String[]{findPageURL})));
        NameValuePair[] pairs = new NameValuePair[]{new NameValuePair("api_user", ConfigUtils.getProperty("sendcloud.system.apiuser")),
                new NameValuePair("api_key", ConfigUtils.getProperty("sendcloud.apikey")),
                new NameValuePair("from", ConfigUtils.getProperty("sendcloud.system.from")), new NameValuePair("fromname", "小幺鸡系统通知"),
                new NameValuePair("subject", "小幺鸡系统通知-找回密码"), new NameValuePair("substitution_vars", vars),
                new NameValuePair("template_invoke_name", "find_password"),};
        String rs = HttpUtils.post(TEMPLATE_URL, pairs);
        if (rs.contains("error")) {
            throw new RuntimeException(rs);
        }
        logger.debug(rs);*/
    }
}
