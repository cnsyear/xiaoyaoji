package cn.xiaoyaoji.service.common;

/**
 * ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 * 　　┃　　　┃神兽保佑
 * 　　┃　　　┃代码无BUG！
 * 　　┃　　　┗━━━┓
 * 　　┃　　　　　　　┣┓
 * 　　┃　　　　　　　┏┛
 * 　　┗┓┓┏━┳┓┏┛
 * 　　　┃┫┫　┃┫┫
 * 　　　┗┻┛　┗┻┛
 * <p>
 * 统一controller返回
 *
 * @author zhoujingjie
 * Date 2018-06-13
 */
public class ResultModel {
    //正确
    public final static int OK = 1;
    //错误
    public final static int ERROR = 0;
    //未登陆
    public final static int NOT_LOGIN = 2;
    /**
     * 返回码；0：正确
     */
    private int code = OK;
    /**
     * 正确时内容
     */
    private Object data;
    /***
     * 消息
     */
    private String msg;

    public ResultModel(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public ResultModel(int code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static ResultModel ok(Object content) {
        return new ResultModel(OK, content);
    }

    public static ResultModel ok() {
        return new ResultModel(OK, null);
    }


    public static ResultModel error(String msg) {
        return new ResultModel(ERROR, null, msg);
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }
}
