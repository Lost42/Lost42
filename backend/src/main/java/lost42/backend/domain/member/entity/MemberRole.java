package lost42.backend.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    MEMBER("ROLE_MEMBER", "일반 유저"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;

    @Override
    public String toString() {
        return String.format("role: %s", this.key);
    }

    public static MemberRole fromString(String key) {
        String roleKey = "ROLE_" + key;
        for (MemberRole role : MemberRole.values()) {
            if (role.getKey().equals(roleKey)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No such role with key: " + roleKey);
    }
}
