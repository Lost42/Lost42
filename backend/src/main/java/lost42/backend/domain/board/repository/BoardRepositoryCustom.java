package lost42.backend.domain.board.repository;

import lost42.backend.domain.board.dto.GetBoardReq;
import lost42.backend.domain.board.entity.Board;

import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> findAllContents(GetBoardReq req);
}
