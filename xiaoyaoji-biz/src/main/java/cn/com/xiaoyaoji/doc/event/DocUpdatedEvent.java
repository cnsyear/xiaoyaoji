package cn.com.xiaoyaoji.doc.event;

import cn.com.xiaoyaoji.data.bean.Doc;
import cn.com.xiaoyaoji.data.bean.User;

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
 * 文档已更新事件
 * @author: zhoujingjie
 * Date: 2018/9/8
 */
public class DocUpdatedEvent {
    private Doc source;
    private Doc now;
    private String remark;
    private User user;

    public DocUpdatedEvent(Doc source, Doc now, User user,String remark) {
        this.source = source;
        this.now = now;
        this.remark = remark;
        this.user = user;
    }

    public Doc getSource() {
        return source;
    }

    public Doc getNow() {
        return now;
    }

    public String getRemark() {
        return remark;
    }

    public User getUser() {
        return user;
    }
}
