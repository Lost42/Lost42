package lost42.backend.domain.board.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lost42.backend.common.code.StatusCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum BoardErrorCode implements StatusCode {
    /**
     * 400 BAD_REQUEST
     */
    INVALID_CONTENT(BAD_REQUEST, "게시글이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
