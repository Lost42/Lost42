package lost42.backend.common.jwt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.config.auth.dto.CustomOAuth2User;
import lost42.backend.config.auth.dto.CustomUserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@Getter
@Builder
@Slf4j
public class JwtTokenInfo {
    private Long memberId;
    private String role;
    private String oauthProvider;
    private Integer oauthId;

    public static JwtTokenInfo fromCustomUserDetails(CustomUserDetails customUserDetails) {
        return JwtTokenInfo.builder()
                .memberId(customUserDetails.getMemberId())
                .role(customUserDetails.getAuthorities().iterator().next().getAuthority().substring("ROLE_".length()))
                .oauthProvider(customUserDetails.getOauthProvider())
                .oauthId(customUserDetails.getOauthId())
                .build();
    }

    public static JwtTokenInfo fromOAuth2User(CustomOAuth2User oAuth2User) {
        log.warn("oAuth2User.getAttributes: {}", oAuth2User.getAttributes());
        return JwtTokenInfo.builder()
                .memberId(oAuth2User.getMemberId())
                .role(oAuth2User.getAuthorities().iterator().next().getAuthority().substring("ROLE_".length()))
                .oauthProvider(oAuth2User.getOauthProvider())
                .oauthId(oAuth2User.getAttribute("id"))
                .build();
    }
}
