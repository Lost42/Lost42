package lost42.backend.common.exception;

import lombok.Getter;
import lost42.backend.common.code.ErrorCode;

@Getter
public class GlobalErrorException extends RuntimeException {
    private final ErrorCode errorCode;

    public GlobalErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return String.format("GlobalErrorException(code=%s, message=%s)",
                errorCode.name(), errorCode.getMessage());
    }
}
