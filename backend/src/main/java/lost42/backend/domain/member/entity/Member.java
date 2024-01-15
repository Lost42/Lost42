package lost42.backend.domain.member.entity;

import lombok.*;
import lost42.backend.config.Auditable;
import lost42.backend.domain.board.entity.Board;
import lost42.backend.domain.member.converter.MemberRoleConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Member extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long memberId;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "password", length = 200)
    private String password;

    @Column(name = "oauth_id", length = 100)
    private Integer oauthId;

    @Column(name = "oauth_provider", length = 20)
    private String oauthProvider;

    @Column(name = "role", nullable = false)
    @Convert(converter = MemberRoleConverter.class)
    private MemberRole role;

    @Column(name = "deleted_dt")
    private LocalDateTime deletedDt;

    public Member update(String name) {
        this.name = name;
        return this;
    }

    public Member changePassword(String password) {
        this.password = password;
        return this;
    }


    public Member resetPassword(String password) {
        this.password = password;
        return this;
    }

    public Member withdrawal() {
        this.deletedDt = LocalDateTime.now();
        return this;
    }

}
