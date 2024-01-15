package lost42.backend.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GetBoardResDto {
    private List<GetBoardRes> contents;
    private Long pageNum;
    private Long totalContents;

    public static GetBoardResDto from(List<GetBoardRes> contents, Long pageNum, Long totalContents) {
        return GetBoardResDto.builder()
                .contents(contents)
                .pageNum(pageNum)
                .totalContents(totalContents)
                .build();
    }
}
