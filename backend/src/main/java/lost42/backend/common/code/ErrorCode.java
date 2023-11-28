package lost42.backend.common.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode implements StatusCode {
    /**
     * 400 BAD_REQUEST: Client 요청이 잘못된 경우
     */
    BAD_REQUEST_ERROR(BAD_REQUEST, "잘못된 요청입니다."),
    REQUEST_HEADER_INVALID(BAD_REQUEST, "잘못된 헤더입니다"),
    REQUEST_BODY_INVALID(BAD_REQUEST, "잘못된 바디입니다"),

    MISSING_REQUEST_PARAMETER(BAD_REQUEST, "요청 파라미터가 전달되지 않았습니다"),
    MISSING_REQUEST_HEADER(BAD_REQUEST, "요청 헤더가 전달되지 않았습니다"),
    MISSING_REQUEST_BODY(BAD_REQUEST, "요청 바디가 전달되지 않았습니다"),

    /**
     * 403 FORBIDDEN: 서버에서 요청을 거부한 경우
     */
    FORBIDDEN_ERROR(FORBIDDEN, "접근 권한이 없습니다"),


    /**
     * 404 NOT_FOUNT: 서버에서 리소스를 찾을 수 없는 경우
     */
    NOT_FOUNT_ERROR(NOT_FOUND, "요청한 자원을 찾을 수 없습니다"),
    NULL_POINTER_ERROR(NOT_FOUND, "Null Pointer Error"),


    /**
     * 500 INTERNAL_SERVER_ERROR: 서버에서 에러가 발생한 경우
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error 발생");

    private final HttpStatus httpStatus;
    private final String message;
}
