package lost42.backend.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateContentRes {
    private Long boardId;

    public static UpdateContentRes success(Long boardId) {
        return UpdateContentRes.builder()
                .boardId(boardId)
                .build();
    }
}
