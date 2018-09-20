package cn.xiaoyaoji.service.exception;

/**
 * @author zhoujingjie
 *         created on 2017/7/1
 */
public class ServiceException extends RuntimeException {

    /**
     * 错误码
     */
    private int errorCode;

    public ServiceException() {
    }


    public ServiceException(String message) {
        super(message);
    }


    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }


    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getErrorCode() {
        return errorCode;
    }
}
