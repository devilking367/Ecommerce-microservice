package deki.ecommerce.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(final String message) {
        super(message);
    }

    public BaseException(final Throwable cause) {
        super(cause);
    }

    public BaseException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
