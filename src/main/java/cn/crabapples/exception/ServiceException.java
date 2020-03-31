package cn.crabapples.exception;

/**
 * TODO 服务异常类
 *
 * @author Mr.He
 * 2019/11/14 21:23
 * e-mail wishforyou.xia@gmail.com
 * qq 294046317
 * pc-name 29404
 */
public class ServiceException extends RuntimeException{
    public ServiceException() {}

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
}
