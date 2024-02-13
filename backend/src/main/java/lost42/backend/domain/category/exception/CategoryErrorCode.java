package lost42.backend.domain.category.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum CategoryErrorCode {
    /**
     * 400 BAD_REQUEST
     */
    INVALID_CATEGORY(BAD_REQUEST, "카테고리가 존재하지 않습니다."),

    /**
     * 401 UNAUTORIZED
     */
    USER_MISS_MATCH(UNAUTHORIZED, "수정 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
