package lost42.backend.domain.board.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {
    FIND("1", "찾습니다"),
    KEEP("2", "보관중입니다");

    private final String code;
    private final String type;

    @Override
    public String toString() {
        return String.format("Type: %s", this.type);
    }

    public static BoardType fromString(String type) {
        for (BoardType boardType : BoardType.values()) {
            if (boardType.getType().equals(type))
                return boardType;
        }
        throw new IllegalArgumentException("Invalid BoardType: " + type);
    }
}
