package lost42.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "로그인 요청 DTO")
public class LoginReq {
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "유효한 이메일 주소 형식이 아닙니다.")
    public String userEmail;
    
    @NotBlank
    public String userPassword;
}
