package lost42.backend.domain.board.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.Response.SuccessResponse;
import lost42.backend.common.auth.dto.CustomUserDetails;
import lost42.backend.domain.board.dto.ChangeTypeReq;
import lost42.backend.domain.board.dto.CreateContentReq;
import lost42.backend.domain.board.dto.GetBoardReq;
import lost42.backend.domain.board.dto.UpdateContentReq;
import lost42.backend.domain.board.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/boards")
public class BoardController {
    private final BoardService boardService;

    // 게시글 조회
//    @PostMapping("")
//    public ResponseEntity<?> getBoards(@Valid @RequestBody GetBoardReq req) {
//        return ResponseEntity.ok().body(boardService.getBoards(req));
//    }

    // 상세 글 조회
    @GetMapping("/getUser")
    public ResponseEntity<?> getUserContent(@RequestParam Long boardId, @AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(boardService.getContent(boardId, securityUser)));
    }

    @GetMapping("/getGuest")
    public ResponseEntity<?> getGuestContent(@RequestParam Long boardId) {
        return ResponseEntity.ok().body(SuccessResponse.from(boardService.getGuestContent(boardId)));
    }

    // 게시글 작성
    @PostMapping("/add")
    public ResponseEntity<?> createContent(@Valid @RequestBody CreateContentReq req, @AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(boardService.createContent(req, securityUser)));
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateContent(@Valid @RequestBody UpdateContentReq req, @AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(boardService.updateContent(req, securityUser)));
    }

    @PatchMapping("/type")
    public ResponseEntity<?> changeType(@Valid @RequestBody ChangeTypeReq req, @AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(boardService.changeType(req, securityUser)));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteContent(@RequestParam Long boardId, @AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
        boardService.deleteContent(boardId, securityUser);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }
}
