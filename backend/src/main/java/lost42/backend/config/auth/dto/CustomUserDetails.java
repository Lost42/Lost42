package lost42.backend.config.auth.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lost42.backend.domain.member.entity.Member;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails implements UserDetails {
    @JsonIgnore
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String userEmail;
    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;
    @JsonIgnore
    private boolean isAccountNonLocked = true;
    @JsonIgnore
    private boolean isAccountNonExpired = true;
    @JsonIgnore
    private boolean isCredentialsNonExpired = true;
    @JsonIgnore
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
