package lost42.backend.domain.board.repository;

import lost42.backend.domain.board.dto.GetBoardReq;
import lost42.backend.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 구현체 : BoardRepositoryImpl
 * QueryDsl 을 사용하기 위한 Custom Repository 적용
 */
public interface BoardRepositoryCustom {
    Page<Board> findAllContents(GetBoardReq req, Pageable pageable);
}
