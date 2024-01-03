package lost42.backend.domain.board.repository;

import lost42.backend.domain.board.dto.GetBoardReq;
import lost42.backend.domain.board.entity.Board;

import java.util.List;

/**
 * 구현체 : BoardRepositoryImpl
 * QueryDsl 을 사용하기 위한 Custom Repository 적용
 */
public interface BoardRepositoryCustom {
    List<Board> findAllContents(GetBoardReq req);
}
