package lost42.backend.domain.board.dto;


import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CreateContentReq {
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

    @NotNull
    private String boardType;
}
