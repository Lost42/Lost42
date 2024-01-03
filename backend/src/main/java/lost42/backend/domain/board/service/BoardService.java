package lost42.backend.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.domain.board.entity.BoardStatus;
import lost42.backend.domain.board.entity.BoardType;
import lost42.backend.domain.board.exception.BoardErrorCode;
import lost42.backend.domain.board.exception.BoardErrorException;
import lost42.backend.domain.member.entity.MemberRole;
import lost42.backend.common.auth.dto.CustomUserDetails;
import lost42.backend.domain.board.dto.*;
import lost42.backend.domain.board.entity.Board;
import lost42.backend.domain.board.repository.BoardRepository;
import lost42.backend.domain.member.entity.Member;
import lost42.backend.domain.member.exception.MemberErrorCode;
import lost42.backend.domain.member.exception.MemberErrorException;
import lost42.backend.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public List<GetBoardRes> getBoards(GetBoardReq req) {
        List<Board> boards = boardRepository.findAllContents(req);
        if (boards == null) {
            throw new BoardErrorException(BoardErrorCode.INVALID_CONTENT);
        }

        List<GetBoardRes> response = new ArrayList<>();

        for (Board board : boards) {
            if (board.getDeletedDt() != null)
                continue;
            response.add(GetBoardRes.fromContent(board));
        }

        return response;
    }

    public GetContentRes getContent(Long boardId, CustomUserDetails securityUser) {
        Board content = isExist(boardId);

        Boolean isWriter = content.getMember().getMemberId().equals(securityUser.getMemberId());
        return GetContentRes.fromContentAndWriter(content, isWriter);
    }

    public GetContentRes getGuestContent(Long boardId) {
        Board content = isExist(boardId);

        return GetContentRes.fromContent(content);
    }

    public CreateContentRes createContent(CreateContentReq req, CustomUserDetails securityUser) {
        Member member = memberRepository.findById(securityUser.getMemberId())
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));

        Board newContent = Board.builder()
                .member(member)
                .name(req.getBoardName())
                .image(req.getBoardImage())
                .foundAt(req.getBoardFoundAt())
                .foundDate(req.getBoardFoundDate())
                .keepingAt(req.getBoardKeepingAt())
                .description(req.getBoardDescription())
                .managedNumber("123123")
                .status(BoardStatus.ING)
                .type(BoardType.FIND)
                .build();

        boardRepository.save(newContent);

        return CreateContentRes.from(newContent);
    }

    public UpdateContentRes updateContent(UpdateContentReq req, CustomUserDetails securityUser) {
        Board content = isExist(req.getBoardId());
        Member member = memberRepository.findById(securityUser.getMemberId())
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));

        if (!member.getRole().equals(MemberRole.ADMIN) && !content.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BoardErrorException(BoardErrorCode.USER_MISS_MATCH);
        }

        content.updateContent(req);

        boardRepository.save(content);

        return UpdateContentRes.success(content.getBoardId());
    }

    public UpdateContentRes changeType(ChangeTypeReq req, CustomUserDetails securityUser) {
        Board content = isExist(req.getBoardId());
        Member member = memberRepository.findById(securityUser.getMemberId())
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));

        if (!member.getRole().equals(MemberRole.ADMIN) && !content.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BoardErrorException(BoardErrorCode.USER_MISS_MATCH);
        }

        content.changeType(req);

        boardRepository.save(content);

        return UpdateContentRes.success(content.getBoardId());
    }

    public void deleteContent(Long boardId, CustomUserDetails securityUser) {
        Board content = isExist(boardId);
        Member member = memberRepository.findById(securityUser.getMemberId())
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));

        if (!member.getRole().equals(MemberRole.ADMIN) && !content.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BoardErrorException(BoardErrorCode.USER_MISS_MATCH);
        }

        content.deleteContent();

        boardRepository.save(content);

    }

    public Board isExist(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardErrorException(BoardErrorCode.INVALID_CONTENT));
    }

}
