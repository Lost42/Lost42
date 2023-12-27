package lost42.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "비밀번호 재설정 요청 DTO")
public class ResetPasswordReq {
    @NotBlank
    private String userPassword;
}
