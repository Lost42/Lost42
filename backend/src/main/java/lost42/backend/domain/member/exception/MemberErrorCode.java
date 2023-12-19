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
    INVALID_USER(BAD_REQUEST, "유저가 존재하지 않습니다"),
    INVALID_PASSWORD(BAD_REQUEST, "사용할 수 없는 비밀번호입니다"),
    INVALID_TOKEN_DATA(BAD_REQUEST, "인증 확인 코드가 일치하지 않습니다"),
    MISS_MATCH_PASSWORD(BAD_REQUEST, "비밀번호와 2차비밀번호가 다릅니다"),
    AUTH_TOKEN_TIME_OUT(BAD_REQUEST, "인증 시간이 초과되었습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
