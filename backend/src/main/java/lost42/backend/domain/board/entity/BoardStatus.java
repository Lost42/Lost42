package lost42.backend.domain.board.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardStatus {
    ING("1", "진행 중"),
    END("2", "완료"),
    DELETE("3", "폐기");

    private final String code;
    private final String status;

    @Override
    public String toString() {
        return String.format("Status: %s", this.status);
    }
}