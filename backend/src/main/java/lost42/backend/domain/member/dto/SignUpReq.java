package lost42.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "일반 회원가입 요청 DTO")
public class SignUpReq {

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "유효한 이메일 주소 형식이 아닙니다.")
    @Schema(description = "유저 이메일", example = "test@example.com")
    private String userEmail;

    @NotBlank
    @Schema(description = "유저 비밀번호")
    private String userPassword;

    @NotBlank
    @Schema(description = "유저 비밀번호 재확인")
    private String userSecondPassword;
}
