package lost42.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "비밀번호 재설정 요청 DTO")
public class SetPasswordReq {
    private String userPassword;
}
