package cn.xiaoyaoji.service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * app常量类
 *
 * @author: zhoujingjie
 * @Date: 17/3/29
 */
public interface AppCts {

    /**
     * token名称
     */
    String TOKEN_NAME = "x-token";

    /**
     * 默认编码
     */
    Charset UTF8 = StandardCharsets.UTF_8;
}
