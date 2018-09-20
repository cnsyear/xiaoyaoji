package cn.xiaoyaoji.api.controller;

import cn.xiaoyaoji.api.base.Session;
import cn.xiaoyaoji.service.annotations.Ignore;
import cn.xiaoyaoji.service.biz.user.service.FindPasswordService;
import cn.xiaoyaoji.service.AppCts;
import cn.xiaoyaoji.service.common.HashMapX;
import cn.xiaoyaoji.service.Message;
import cn.xiaoyaoji.service.common.Result;
import cn.xiaoyaoji.service.spi.EmailService;
import cn.xiaoyaoji.service.util.AssertUtils;
import cn.xiaoyaoji.service.util.ConfigUtils;
import cn.xiaoyaoji.service.util.StringUtils;
import cn.xiaoyaoji.service.biz.user.bean.FindPassword;
import cn.xiaoyaoji.service.biz.user.bean.UserThirdParty;
import cn.xiaoyaoji.service.biz.user.bean.User;
import cn.xiaoyaoji.service.util.CacheUtils;
import cn.xiaoyaoji.service.biz.user.service.UserService;
import cn.xiaoyaoji.api.utils.PasswordUtils;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 修改
     *
     * @param user       当前登录用户
     * @param updateUser 需要更新的用户信息
     * @return
     */
    @PostMapping()
    public Object update(@Session User user, User updateUser,
                         @CookieValue(AppCts.TOKEN_NAME) String token) {
        updateUser.setId(user.getId());
        updateUser.setPassword(null);
        updateUser.setType(null);
        updateUser.setStatus(null);

        int rs = userService.updateSelective(updateUser);
        AssertUtils.isTrue(rs > 0, "操作失败");
        user = userService.findOne(user.getId());
        CacheUtils.putUser(token, user);
        return rs;
    }

    @Ignore
    @PostMapping("register")
    public Object create(User user) {
        user.setEmail(user.getEmail().trim());
        AssertUtils.isTrue(StringUtils.isEmail(user.getEmail()), "请输入有效的邮箱");
        AssertUtils.notNull(user.getPassword(), "请输入密码");
        AssertUtils.notNull(user.getEmail(), "请输入邮箱");
        // 检查账号是否已存在
        AssertUtils.isTrue(!userService.checkEmailExists(user.getEmail()), Message.EMAIL_EXISTS);
        user.setPassword(PasswordUtils.password(user.getPassword()));
        user.setType(User.Type.USER);
        user.setId(StringUtils.id());
        user.setAvatar("/assets/img/defaultlogo.jpg");
        user.setStatus(1);
        int rs = userService.insert(user);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return true;
    }

    @GetMapping("search")
    public Object search(@Session User user, @RequestParam("key") String key) {
        if (key == null || key.trim().length() == 0) {
            return null;
        }
        List<User> users = userService.searchUsers(key, user.getId());
        return new HashMapX<>().append("users", users);
    }

    @GetMapping("projectUsers")
    public Object getAllProjectUsers(@Session User user) {
        List<User> users = userService.getProjectRelationUser(user.getId());
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
     * 修改密码
     *
     * @return
     */
    @PostMapping("password")
    public Object updatePassword(@Session User user, @RequestParam String password) {
        User temp = new User();
        temp.setId(user.getId());
        temp.setPassword(PasswordUtils.password(password));
        AssertUtils.notNull(password, "密码为空");
        int rs = userService.updateSelective(temp);
        AssertUtils.isTrue(rs > 0, Message.OPER_ERR);
        return rs;
    }

    /**
     * 找回密码2
     *
     * @return
     */
    @Ignore
    @PostMapping("newpassword")
    public Object newPassword(@RequestParam String email, @RequestParam String id, @RequestParam String password) {
        AssertUtils.notNull(email, "邮箱为空");
        AssertUtils.notNull(id, "无效请求");
        AssertUtils.notNull(password, "密码为空");
        AssertUtils.isTrue(StringUtils.isEmail(email), "邮箱格式错误");
        password = PasswordUtils.password(password);
        int rs = userService.updateNewPassword(id, email, password);
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
        CacheUtils.put(token, "emailCaptcha", code);
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
        String captcha = (String) CacheUtils.get(token, "emailCaptcha");
        AssertUtils.isTrue(code.equals(captcha), "验证码错误");
        // 检查邮箱是否存在
        AssertUtils.isTrue(!userService.checkEmailExists(email), "该邮箱已存在");
        User user = CacheUtils.getUser(token);
        User temp = new User();
        temp.setId(user.getId());
        temp.setEmail(email);
        int rs = userService.updateSelective(temp);
        AssertUtils.isTrue(rs > 0, "操作失败");
        CacheUtils.putUser(token, userService.findOne(user.getId()));
        CacheUtils.remove(token, "emailCaptcha");
        return rs;
    }

    /**
     * 绑定第三方
     *
     * @return
     */
    @PostMapping("bind")
    public Object bind(@Session User user, @RequestParam String accessToken,
                       @RequestParam String type,
                       @RequestParam String thirdpartyId,
                       @CookieValue(AppCts.TOKEN_NAME) String token
    ) {

        UserThirdParty userThirdParty = new UserThirdParty();
        userThirdParty.setUserId(user.getId());
        userThirdParty.setType(type);
        userThirdParty.setId(thirdpartyId);
        int rs = userService.bindUserWithThirdParty(userThirdParty);
        AssertUtils.isTrue(rs > 0, "操作失败");
        return true;
    }

    @PostMapping("unbind/{pluginId}")
    public Object unbind(@Session User user, @PathVariable("pluginId") String pluginId, @RequestHeader(AppCts.TOKEN_NAME) String token) {
        int rs = userService.deleteThirdly(user.getId(), pluginId);
        AssertUtils.isTrue(rs > 0, "操作失败");
        return new HashMapX<>()
                .append("user", user)
                ;
    }

    @PostMapping("avatar")
    public Object uploadAvatar(@Session User user, @RequestParam("avatar") MultipartFile file, @RequestHeader(AppCts.TOKEN_NAME) String token) throws IOException {
        String fileAccess = ConfigUtils.getFileAccessURL();
/*
        if (file != null && file.getSize() > 0 && file.getContentType().startsWith("image")) {

            MetaData md = FileUtils.upload(file);
            User temp = new User();
            temp.setAvatar(md.getPath());
            temp.setId(user.getId());
            int rs = userService.updateSelective(temp);
            AssertUtils.isTrue(rs > 0, "上传失败");
            if (!Strings.isNullOrEmpty(user.getAvatar()) && user.getAvatar().startsWith(fileAccess)) {
                String url = user.getAvatar().substring(fileAccess.length());
                try {
                    FileUtils.delete(url);
                } catch (IOException e) {
                }
            }

            user.setAvatar(temp.getAvatar());
            CacheUtils.putUser(token, user);
            return new HashMapX<>().append("avatar", ConfigUtils.getFileAccessURL() + user.getAvatar());
        }
        return new Result<>(false, "请上传图片");*/
        return null;
    }

}
