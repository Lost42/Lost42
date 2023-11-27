package lost42.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "로그인 요청 DTO")
public class LoginReq {
    public String userEmail;
    public String userPassword;
}
