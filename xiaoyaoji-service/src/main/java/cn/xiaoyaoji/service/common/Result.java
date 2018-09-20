package cn.xiaoyaoji.service.common;

/**
 * @author: zhoujingjie
 * @Date: 16/5/4
 * @see ResultModel
 */
@Deprecated
public class Result<T> {
    private int code;
    private T data;
    private String msg;

    public static final int OK = 0;
    public static final int ERROR = -1;
    public static final int NOT_LOGIN = -2;

    public Result() {
    }

    public Result(boolean result, T data) {
        if(result){
            this.code = OK;
            this.data = data;
        }else{
            this.code = ERROR;
            this.msg = (String) data;
        }
    }
    public Result(int code,String msg){
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
