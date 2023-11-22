package lost42.backend.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lost42.backend.config.auth.MemberRole;
import lost42.backend.domain.member.entity.Member;

import java.util.Map;

@Getter
public class OauthAttribute {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OauthAttribute(Map<String, Object> attributes,
                          String nameAttributeKey,
                          String name,
                          String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    public static OauthAttribute of(String registrarionId,
                                    String userNameAttributeName,
                                    Map<String, Object> attributes) {
        return of42(userNameAttributeName, attributes);
    }

    private static OauthAttribute of42(String userNameAttributeName,
                                       Map<String, Object> attributes) {
        return OauthAttribute.builder()
                .name((String) attributes.get("login"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .oauthProvider("42Seoul")
                .role(MemberRole.MEMBER)
                .build();
    }
}
