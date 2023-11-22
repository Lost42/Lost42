package lost42.backend.config.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lost42.backend.domain.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;
    private String userEmail;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isAccountNonLocked = true;
    private boolean isAccountNonExpired = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;

    public CustomUserDetails(Member member) {
        this.username = member.getName();
        this.password = member.getPassword();
        this.userEmail = member.getEmail();
        this.authorities = Arrays.stream(new String[]{member.getRole().getKey()})
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }
}
