package lost42.backend.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lost42.backend.domain.board.entity.BoardStatus;
import lost42.backend.domain.board.entity.BoardType;
import lost42.backend.domain.member.entity.MemberRole;
import lost42.backend.domain.board.entity.Board;

import java.time.LocalDateTime;

@Builder
@Getter
public class GetContentRes {
    private ContentRes contentRes;
    private MemberRes memberRes;

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

        public static ContentRes from(Board content) {
            return ContentRes.builder()
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
        private Boolean isWriter;

        public static MemberRes fromContentAndWriter(Board content, Boolean isWriter) {
            return MemberRes.builder()
                    .userName(content.getMember().getName())
                    .userRole(content.getMember().getRole())
                    .isWriter(isWriter)
                    .build();
        }

        public static MemberRes fromContent(Board content) {
            return MemberRes.builder()
                    .userName(content.getMember().getName())
                    .userRole(content.getMember().getRole())
                    .isWriter(false)
                    .build();
        }
    }

    public static GetContentRes fromContentAndWriter(Board content, Boolean isWriter) {
        return GetContentRes.builder()
                .contentRes(ContentRes.from(content))
                .memberRes(MemberRes.fromContentAndWriter(content, isWriter))
                .build();
    }

    public static GetContentRes fromContent(Board content) {
        return GetContentRes.builder()
                .contentRes(ContentRes.from(content))
                .memberRes(MemberRes.fromContent(content))
                .build();
    }

}
