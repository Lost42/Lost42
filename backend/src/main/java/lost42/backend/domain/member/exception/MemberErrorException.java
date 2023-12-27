package lost42.backend.domain.member.exception;

import lombok.Getter;

@Getter
public class MemberErrorException extends RuntimeException {
    private final MemberErrorCode errorCode;

    public MemberErrorException(MemberErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return String.format("AuthErrorException(code=%s, message=%s)",
                errorCode.name(), errorCode.getMessage());
    }
}
