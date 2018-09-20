package cn.xiaoyaoji.api.utils;

import cn.xiaoyaoji.api.Config;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * @author zhoujingjie
 *         created on 2017/7/1
 */
public class PasswordUtils {
    public static String password(String password) {
        return Hashing.md5().hashString(Config.SALT + password, StandardCharsets.UTF_8).toString();
    }
}
