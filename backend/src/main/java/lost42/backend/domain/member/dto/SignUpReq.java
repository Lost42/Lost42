package lost42.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "일반 회원가입 요청 DTO")
public class SignUpReq {
    private String userEmail;
    private String userName;
    private String userPassword;
    private String contract;
}
