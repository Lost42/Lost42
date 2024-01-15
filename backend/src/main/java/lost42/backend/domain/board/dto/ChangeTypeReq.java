package lost42.backend.domain.board.dto;

import lombok.Getter;

@Getter
public class ChangeTypeReq {
    private Long boardId;
    private String boardType;
}
