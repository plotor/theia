package org.zhenchao.kratos.error;

/**
 * @author zhenchao.wang 2020-01-10 15:06
 * @version 1.0.0
 */
public class ConvertException extends Exception {

    private static final long serialVersionUID = 3990949217950210964L;

    public ConvertException() {
    }

    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertException(Throwable cause) {
        super(cause);
    }

    public ConvertException(String message, Throwable cause,
                            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
