package lost42.backend.domain.board.exception;

import lombok.Getter;

@Getter
public class BoardErrorException extends RuntimeException {
    private final BoardErrorCode errorCode;

    public BoardErrorException(BoardErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return String.format("BoardErrorException(code=%s, message=%s)",
                errorCode.name(), errorCode.getMessage());
    }
}
