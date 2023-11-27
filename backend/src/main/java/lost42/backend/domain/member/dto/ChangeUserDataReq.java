package lost42.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "유저 데이터 변경 요청 DTO")
public class ChangeUserDataReq {
    private String userPassword;
    private String userContract;
}
