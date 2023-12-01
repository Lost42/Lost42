package lost42.backend.config.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.config.auth.dto.CustomUserDetails;
import lost42.backend.domain.member.entity.Member;
import lost42.backend.domain.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        log.info("Custom User Details Service Location");
        Member member = memberRepository.findByEmail(userEmail).orElse(null); // TODO 공통 에러 핸들링으로 적용
        if (member == null) {
            throw new EntityNotFoundException("member does not exist");
        }

        CustomUserDetails customUser = new CustomUserDetails(member);

        log.warn("Custom User Detail Service: {}", customUser);
        return customUser;
    }
}
