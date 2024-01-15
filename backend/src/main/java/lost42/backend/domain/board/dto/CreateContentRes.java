package lost42.backend.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lost42.backend.domain.board.entity.Board;

@Builder
@Getter
public class CreateContentRes {
    private Long boardId;

    public static CreateContentRes from(Board newContent) {
        return CreateContentRes.builder()
                .boardId(newContent.getBoardId())
                .build();
    }
}
