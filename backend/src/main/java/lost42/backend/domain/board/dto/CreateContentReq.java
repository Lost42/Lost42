package lost42.backend.domain.board.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CreateContentReq {
    @NotBlank
    @Schema(description = "글 제목", example = "게시글 테스트")
    private String boardName;

    @NotBlank
    private String boardFoundAt;

    @NotBlank
    private String boardFoundDate;

    @NotBlank
    private String boardKeepingAt;

    private String boardDescription;

    @NotNull
    private String boardType;
}
