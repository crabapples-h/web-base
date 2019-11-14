package cn.crabapples.exception;

/**
 * @author hequan@gogpay.cn
 * @date 2019/7/23 15:58
 */
public class NotFoundServiceException extends RuntimeException {
    public NotFoundServiceException() {
        super();
    }

    public NotFoundServiceException(String message) {
        super(message);
    }

    public NotFoundServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundServiceException(Throwable cause) {
        super(cause);
    }

    protected NotFoundServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
