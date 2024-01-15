package lost42.backend.domain.member.converter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.domain.member.entity.MemberRole;

import javax.persistence.AttributeConverter;

@Getter
@Slf4j
public class MemberRoleConverter implements AttributeConverter<MemberRole, String> {

    @Override
    public String convertToDatabaseColumn(MemberRole attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public MemberRole convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (MemberRole role : MemberRole.values()) {
            if (role.getCode().equals(dbData)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}
