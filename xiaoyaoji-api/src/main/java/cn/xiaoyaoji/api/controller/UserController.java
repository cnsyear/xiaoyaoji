package cn.xiaoyaoji.api.controller;

import cn.xiaoyaoji.api.base.Session;
import cn.xiaoyaoji.api.utils.PasswordUtils;
import cn.xiaoyaoji.service.AppCts;
import cn.xiaoyaoji.service.Message;
import cn.xiaoyaoji.service.XyjProperties;
import cn.xiaoyaoji.service.annotations.Ignore;
import cn.xiaoyaoji.service.biz.user.bean.FindPassword;
import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.service.biz.user.bean.UserThirdParty;
import cn.xiaoyaoji.service.biz.user.service.FindPasswordService;
import cn.xiaoyaoji.service.biz.user.service.UserService;
import cn.xiaoyaoji.service.common.HashMapX;
import cn.xiaoyaoji.service.common.ResultModel;
import cn.xiaoyaoji.service.spi.CacheService;
import cn.xiaoyaoji.service.spi.EmailService;
import cn.xiaoyaoji.service.spi.FileUploadService;
import cn.xiaoyaoji.service.util.AssertUtils;
import cn.xiaoyaoji.service.util.BeanUtils;
import cn.xiaoyaoji.service.util.StringUtils;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author zhoujingjie
 * @date 2016-05-31
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FindPasswordService findPasswordService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private CacheService cacheService;

    @Autowired
    private XyjProperties xyjProperties;

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 修改
     *
     * @param user       当前登录用户
     * @param updateUser 需要更新的用户信息
     * @return
     */
    @PostMapping()
    public Object update(@Session User user, User updateUser,
                         @RequestHeader(AppCts.TOKEN_NAME) String token) {
        updateUser.setId(user.getId());
        if (!Strings.isNullOrEmpty(user.getPassword())) {
            updateUser.setPassword(PasswordUtils.password(updateUser.getPassword()));
        }
        updateUser.setType(null);
        updateUser.setStatus(null);
        BeanUtils.copyProperties(updateUser, user);
        int rs = userService.updateSelective(user);
        cacheService.cacheUser(token, user);
        return rs;
    }

    /**
     * 注册
     */
    @Ignore
    @PostMapping("register")
    public Object create(User user) {
        AssertUtils.notNull(user.getEmail(), "请输入有效邮箱");
        user.setEmail(user.getEmail().trim());
        AssertUtils.notNull(user.getPassword(), "请输入密码");
        AssertUtils.notNull(user.getEmail(), "请输入邮箱");
        // 检查账号是否已存在
        AssertUtils.isTrue(!userService.checkEmailExists(user.getEmail()), Message.EMAIL_EXISTS);
        user.setPassword(PasswordUtils.password(user.getPassword()));
        user.setType(User.Type.USER);
        user.setId(StringUtils.id());
        user.setAvatar(xyjProperties.getFileAccess() + "assets/img/defaultlogo.jpg");
        user.setStatus(1);
        int rs = userService.insert(user);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return true;
    }

    /**
     * 搜索用户
     *
     * @param user
     * @param key
     * @return
     */
    @GetMapping("search")
    public Object search(@Session User user, @RequestParam("key") String key) {
        if (key == null || key.trim().length() == 0) {
            return null;
        }
        List<User> users = userService.searchUsers(key, user.getId());
        return new HashMapX<>().append("users", users);
    }


    /**
     * 找回密码1
     *
     * @return
     */
    @Ignore
    @PostMapping("findpassword")
    public Object findPassword(@RequestParam String email) {
        AssertUtils.notNull(email, "邮箱为空");
        AssertUtils.isTrue(StringUtils.isEmail(email), "邮箱格式错误");
        AssertUtils.isTrue(userService.checkEmailExists(email), "邮箱不存在");
        FindPassword fp = new FindPassword();
        fp.setIsUsed(0);
        fp.setEmail(email);
        fp.setCreateTime(new Date());
        fp.setId(StringUtils.id());
        int rs = findPasswordService.insert(fp);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        emailService.sendFindPassword(fp.getId(), email);
        return rs;
    }

    /**
     * 找回密码2
     *
     * @return
     */
    @Ignore
    @PostMapping("newpassword")
    public Object newPassword(@RequestParam String email, @RequestParam String findPasswordToken, @RequestParam String password) {
        AssertUtils.notNull(email, "邮箱为空");
        AssertUtils.notNull(findPasswordToken, "无效请求");
        AssertUtils.notNull(password, "密码为空");
        AssertUtils.isTrue(StringUtils.isEmail(email), "邮箱格式错误");
        password = PasswordUtils.password(password);
        int rs = userService.updateNewPassword(findPasswordToken, email, password);
        AssertUtils.isTrue(rs > 0, "操作失败");
        return 1;
    }


    /**
     * 发送邮箱验证码
     *
     * @return
     */
    @Ignore
    @PostMapping("email/captcha")
    public Object sendEmailCaptcha(@RequestParam String email, @RequestHeader(AppCts.TOKEN_NAME) String token) {
        String code = StringUtils.code();
        AssertUtils.notNull(email, "邮箱为空");
        AssertUtils.isTrue(StringUtils.isEmail(email), "邮箱格式错误");
        emailService.sendCaptcha(code, email);
        cacheService.set("email-captcha:" + token, code);
        return true;
    }

    /**
     * 新邮件
     *
     * @return
     */
    @PostMapping("email/new")
    public Object newEmail(@RequestParam String code, @RequestParam String email, @RequestHeader(AppCts.TOKEN_NAME) String token) {
        AssertUtils.notNull(code, "验证码为空");
        AssertUtils.notNull(email, "邮箱为空");
        AssertUtils.isTrue(StringUtils.isEmail(email), "邮箱格式错误");
        String captcha = cacheService.get("captcha:" + token, String.class);
        AssertUtils.isTrue(code.equals(captcha), "验证码错误");
        // 检查邮箱是否存在
        AssertUtils.isTrue(!userService.checkEmailExists(email), "该邮箱已存在");
        User user = cacheService.getUser(token);
        User temp = new User();
        temp.setId(user.getId());
        temp.setEmail(email);
        int rs = userService.updateSelective(temp);
        AssertUtils.isTrue(rs > 0, "操作失败");
        cacheService.cacheUser(token, userService.findOne(user.getId()));
        cacheService.remove("email-captcha::" + token);
        return rs;
    }

    /**
     * 绑定第三方
     *
     * @return
     */
    @PostMapping("bind")
    public Object bind(@Session User user, @RequestParam String accessToken,
                       @RequestParam String pluginId,
                       @RequestParam String thirdpartyId,
                       @RequestHeader(AppCts.TOKEN_NAME) String token
    ) {

        UserThirdParty userThirdParty = new UserThirdParty();
        userThirdParty.setUserId(user.getId());
        userThirdParty.setType(pluginId);
        userThirdParty.setThirdId(thirdpartyId);
        int rs = userService.bindUserWithThirdParty(userThirdParty);
        AssertUtils.isTrue(rs > 0, "操作失败");
        return true;
    }

    /**
     * 解绑第三方关联
     *
     * @param user     当前用户
     * @param pluginId 插件id
     * @param token    会话token
     * @return user
     */
    @PostMapping("unbind/{pluginId}/do")
    public Object unbind(@Session User user, @PathVariable("pluginId") String pluginId, @RequestHeader(AppCts.TOKEN_NAME) String token) {
        int rs = userService.deleteThirdly(user.getId(), pluginId);
        AssertUtils.isTrue(rs > 0, "操作失败");
        return rs;
    }

    /**
     * 修改头像
     *
     * @param user
     * @param file
     * @param token
     * @return
     * @throws IOException
     */
    @PostMapping("avatar")
    public Object uploadAvatar(@Session User user, @RequestParam("avatar") MultipartFile file, @RequestHeader(AppCts.TOKEN_NAME) String token) throws IOException {
        if (file != null && file.getSize() > 0 && file.getContentType().startsWith("image")) {
            String path = fileUploadService.upload(file.getInputStream(), file.getContentType());
            User temp = new User();
            temp.setAvatar(xyjProperties.getFileAccess() + path);
            temp.setId(user.getId());
            int rs = userService.updateSelective(temp);
            AssertUtils.isTrue(rs > 0, "上传失败");
            if (!Strings.isNullOrEmpty(user.getAvatar())) {
                String url = user.getAvatar();
                if (url.startsWith(xyjProperties.getFileAccess())) {
                    url = url.substring(xyjProperties.getFileAccess().length());
                }
                fileUploadService.delete(url);
            }
            user.setAvatar(temp.getAvatar());
            cacheService.cacheUser(token, user);
            return new HashMapX<>().append("avatar", user.getAvatar());
        }
        return ResultModel.error("请上传图片");
    }

}
