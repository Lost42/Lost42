package lost42.backend.domain.category.exception;

import lombok.Getter;
import lost42.backend.domain.board.exception.BoardErrorCode;

@Getter
public class CategoryErrorException extends RuntimeException {
    private final CategoryErrorCode errorCode;

    public CategoryErrorException(CategoryErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return String.format("CategoryErrorException(code=%s, message=%s)",
                errorCode.name(), errorCode.getMessage());
    }
}
