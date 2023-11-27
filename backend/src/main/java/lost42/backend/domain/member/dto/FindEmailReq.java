package lost42.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "비밀번호 찾기 요청 DTO")
public class FindEmailReq {
    private String userName;
    private String userContract;
}
