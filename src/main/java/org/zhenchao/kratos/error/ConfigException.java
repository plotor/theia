package org.zhenchao.kratos.error;

/**
 * @author zhenchao.wang 2017-09-05 16:29:19
 * @version 1.0.0
 */
public class ConfigException extends Exception {

    private static final long serialVersionUID = -265369713951433169L;

    public ConfigException() {
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }

    public ConfigException(String message, Throwable cause,
                           boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
