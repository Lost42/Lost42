package lost42.backend.domain.member.dto;

import lombok.*;
import lost42.backend.domain.member.entity.MemberRole;
import lost42.backend.domain.member.entity.Member;

@Builder
@Getter
@AllArgsConstructor
public class UserInfoRes {
    private String userEmail;
    private String userName;
    private MemberRole userRole;

    public static UserInfoRes fromMember(Member member) {
        return UserInfoRes.builder()
                .userEmail(member.getEmail())
                .userName(member.getName())
                .userRole(member.getRole())
                .build();
    }
}
