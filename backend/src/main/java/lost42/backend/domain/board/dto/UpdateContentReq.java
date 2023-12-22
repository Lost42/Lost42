package lost42.backend.domain.board.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * 	"boardName": "게시글1",
 * 	"boardImage": multipartform,
 * 	"boardFoundAt": "7클러스터",
 * 	"boardFoundDate": "2023-11-10",
 * 	"boardkeepingAt": "개포 안내 데스크",
 * 	"boardDescription": "블라블라
 */

@Getter
public class UpdateContentReq {
    private Long BoardId;
    @NotBlank
    private String boardName;
    private String boardImage;
    @NotBlank
    private String boardFoundAt;
    @NotBlank
    private String boardFoundDate;
    @NotBlank
    private String boardKeepingAt;
    private String boardDescription;
}
