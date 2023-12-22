package lost42.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lost42.backend.domain.member.entity.MemberRole;
import lost42.backend.domain.member.entity.Member;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "회원가입 응답 api")
public class SignUpRes {
    private String userEmail;
    private String userName;
    private MemberRole userRole;

    public static SignUpRes from(Member member) {
        return SignUpRes.builder()
                .userEmail(member.getEmail())
                .userName(member.getName())
                .userRole(member.getRole())
                .build();
    }
}
