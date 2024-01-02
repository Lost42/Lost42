package lost42.backend.common.handler;

import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.response.ErrorResponse;
import lost42.backend.common.exception.GlobalErrorException;
import lost42.backend.domain.board.exception.BoardErrorException;
import lost42.backend.domain.member.exception.MemberErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ProjectErrorExceptionHandler {

    /**
     * GlobalError Exception
     */
    @ExceptionHandler(GlobalErrorException.class)
    protected ResponseEntity<ErrorResponse> handleGlobalErrorException(GlobalErrorException e) {
        log.warn("handleGlobalErrorException : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
    }

    /**
     * MemberError Exception
     */
    @ExceptionHandler(MemberErrorException.class)
    protected ResponseEntity<ErrorResponse> handleMemberErrorException(MemberErrorException e) {
        log.warn("handleMemberErrorException : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(BoardErrorException.class)
    protected ResponseEntity<ErrorResponse> handleBoardErrorException(BoardErrorException e) {
        log.warn("handleBoardErrorException : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
    }
}
