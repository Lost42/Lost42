package lost42.backend.domain.board.entity;

import lombok.Getter;

import javax.persistence.AttributeConverter;

@Getter
public class BoardStatusConverter implements AttributeConverter<BoardStatus, String> {

    @Override
    public String convertToDatabaseColumn(BoardStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public BoardStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (BoardStatus boardStatus : BoardStatus.values()) {
            if (boardStatus.getCode().equals(dbData)) {
                return boardStatus;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}
