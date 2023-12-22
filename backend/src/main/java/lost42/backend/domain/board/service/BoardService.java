package lost42.backend.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

//    public Object getBoards(GetBoardReq req) {
//
//    }

    public GetContentRes getContent(Long boardId, CustomUserDetails securityUser) {
        Board content = boardRepository.findById(boardId).orElse(null);

        Boolean isWriter = content.getMember().getMemberId() == securityUser.getMemberId();
        return GetContentRes.fromContentAndWriter(content, isWriter);
    }

    public GetContentRes getGuestContent(Long boardId) {
        Board content = boardRepository.findById(boardId).orElse(null);

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
                .managedNumber("123123") // 추후 enum 클래스로 변경
                .status("보관 중") // 추후 enum 클래스로 변경 변경
                .type(req.getBoardType())
                .build();

        boardRepository.save(newContent);

        return CreateContentRes.from(newContent);
    }

    public UpdateContentRes updateContent(UpdateContentReq req, CustomUserDetails securityUser) {
        Board content = boardRepository.findById(req.getBoardId()).orElse(null);
        Member member = memberRepository.findById(securityUser.getMemberId())
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));

        if (!member.getRole().equals(MemberRole.ADMIN) && content.getMember().getMemberId() != member.getMemberId()) {
            throw new RuntimeException("수정 권한 없음");
        }

        content.updateContent(req);

        boardRepository.save(content);

        return UpdateContentRes.success(content.getBoardId());
    }

    public UpdateContentRes changeType(ChangeTypeReq req, CustomUserDetails securityUser) {
        Board content = boardRepository.findById(req.getBoardId()).orElse(null);
        Member member = memberRepository.findById(securityUser.getMemberId())
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));

        if (!member.getRole().equals(MemberRole.ADMIN) && content.getMember().getMemberId() != member.getMemberId()) {
            throw new RuntimeException("수정 권한 없음");
        }

        content.changeType(req);

        boardRepository.save(content);

        return UpdateContentRes.success(content.getBoardId());
    }

    public Boolean deleteContent(Long boardId, CustomUserDetails securityUser) {
        Board content = boardRepository.findById(boardId).orElse(null);
        Member member = memberRepository.findById(securityUser.getMemberId())
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));

        if (!member.getRole().equals(MemberRole.ADMIN) && content.getMember().getMemberId() != member.getMemberId()) {
            throw new RuntimeException("수정 권한 없음");
        }

        content.deleteContent();

        boardRepository.save(content);

        return true;
    }

}
