package lost42.backend.common.auth.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lost42.backend.common.code.StatusCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AuthErrorCode implements StatusCode {
    /**
     * 400 BAD_REQUEST
     */
    INVALID_HEADER(BAD_REQUEST, "유효하지 않은 헤더입니다"),
    EMPTY_ACCESS_TOKEN(BAD_REQUEST, "액세스 토큰이 비어있습니다"),
    EMPTY_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 비어있습니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다"),

    /**
     * 401 UNAUTHORIZED
     */
    FAILED_AUTHENTICATION(UNAUTHORIZED, "인증에 실패하였습니다"),
    TAMPERED_ACCESS_TOKEN(UNAUTHORIZED, "서명이 조작된 토큰입니다"),
    EXPIRED_ACCESS_TOKEN(UNAUTHORIZED, "사용기간이 만료된 토큰입니다"),
    MALFORMED_ACCESS_TOKEN(UNAUTHORIZED, "비정상적인 토큰입니다"),
    WRONG_JWT_TOKEN(UNAUTHORIZED, "잘못된 토큰입니다(default)"),
    UNSUPPORTED_JWT_TOKEN(UNAUTHORIZED, "지원하지 않는 토큰입니다"),
    USER_NOT_FOUND(UNAUTHORIZED, "존재하지 않는 유저입니다"),
    NEED_LOGIN(UNAUTHORIZED, "로그인을 해야 합니다."),

    /**
     * 403 FORBIDDEN
     */
    FORBIDDEN_ACCESS_TOKEN(FORBIDDEN, "해당 토큰에는 엑세스 권한이 없습니다"),
    MISMATCHED_REFRESH_TOKEN(FORBIDDEN, "리프레시 토큰의 유저 정보가 일치하지 않습니다");


    private final HttpStatus httpStatus;
    private final String message;
}
