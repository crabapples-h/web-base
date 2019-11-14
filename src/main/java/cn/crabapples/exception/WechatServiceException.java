package cn.crabapples.exception;

/**
 * TODO 微信服务异常类
 *
 * @author Mr.He
 * @date 2019/11/14 21:23
 * e-mail wishforyou.xia@gmail.com
 * qq 294046317
 * pc-name 29404
 */
public class WechatServiceException extends RuntimeException{
    public WechatServiceException() {}

    public WechatServiceException(String message) {
        super(message);
    }

    public WechatServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public WechatServiceException(Throwable cause) {
        super(cause);
    }

    public WechatServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
