package lost42.backend.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ADMIN("1", "ROLE_ADMIN"),
    MEMBER("2", "ROLE_MEMBER");

    private final String code;
    private final String role;

    @Override
    public String toString() {
        return String.format("role: %s", this.role);
    }

    public static MemberRole fromString(String key) {
        String roleKey = "ROLE_" + key;
        for (MemberRole role : MemberRole.values()) {
            if (role.getRole().equals(roleKey)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No such role with key: " + roleKey);
    }
}
