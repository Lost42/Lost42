package lost42.backend.domain.board.converter;

import lombok.Getter;
import lost42.backend.domain.board.entity.BoardType;

import javax.persistence.AttributeConverter;

@Getter
public class BoardTypeConverter implements AttributeConverter<BoardType, String> {

    @Override
    public String convertToDatabaseColumn(BoardType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public BoardType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (BoardType boardType : BoardType.values()) {
            if (boardType.getCode().equals(dbData)) {
                return boardType;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}
