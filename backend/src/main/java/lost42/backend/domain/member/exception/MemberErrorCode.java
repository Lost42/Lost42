package lost42.backend.domain.member.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lost42.backend.common.code.StatusCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MemberErrorCode implements StatusCode {
    /**
     * 400 BAD_REQUEST
     */
    INVALID_USER(BAD_REQUEST, "유저가 존재하지 않습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
