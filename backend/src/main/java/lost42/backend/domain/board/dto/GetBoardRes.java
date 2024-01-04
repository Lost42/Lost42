package lost42.backend.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lost42.backend.domain.board.entity.Board;
import lost42.backend.domain.board.entity.BoardStatus;
import lost42.backend.domain.board.entity.BoardType;
import lost42.backend.domain.member.entity.MemberRole;

import java.time.LocalDateTime;

@Builder
@Getter
public class GetBoardRes {
    private GetBoardRes.ContentRes board;
    private GetBoardRes.MemberRes user;

    @Builder
    @Getter
    private static class ContentRes {
        private Long boardId;
        private String boardName;
        private String boardImage;
        private String boardFoundAt;
        private String boardFoundDate;
        private String boardKeepingAt;
        private String boardDescription;
        private String boardManagedNumber;
        private BoardStatus boardStatus;
        private BoardType boardType;
        private LocalDateTime boardCreatedAt;

        public static GetBoardRes.ContentRes from(Board content) {
            return GetBoardRes.ContentRes.builder()
                    .boardId(content.getBoardId())
                    .boardName(content.getName())
                    .boardImage(content.getImage())
                    .boardFoundAt(content.getFoundAt())
                    .boardFoundDate(content.getFoundDate())
                    .boardKeepingAt(content.getKeepingAt())
                    .boardDescription(content.getDescription())
                    .boardManagedNumber(content.getManagedNumber())
                    .boardStatus(content.getStatus())
                    .boardType(content.getType())
                    .boardCreatedAt(content.getCreatedAt())
                    .build();
        }
    }

    @Builder
    @Getter
    private static class MemberRes {
        private String userName;
        private MemberRole userRole;

        public static GetBoardRes.MemberRes fromContent(Board content) {
            return GetBoardRes.MemberRes.builder()
                    .userName(content.getMember().getName())
                    .userRole(content.getMember().getRole())
                    .build();
        }
    }

    public static GetBoardRes fromContent(Board content) {
        return GetBoardRes.builder()
                .board(ContentRes.from(content))
                .user(GetBoardRes.MemberRes.fromContent(content))
                .build();
    }
}
