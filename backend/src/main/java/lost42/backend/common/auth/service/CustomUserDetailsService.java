package lost42.backend.common.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.auth.dto.CustomUserDetails;
import lost42.backend.domain.member.entity.Member;
import lost42.backend.domain.member.exception.MemberErrorCode;
import lost42.backend.domain.member.exception.MemberErrorException;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));
        if (member == null) {
            throw new EntityNotFoundException("member does not exist");
        }

        return new CustomUserDetails(member);
    }

    public UserDetails loadUserByMemberId(Long memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));
        if (member == null) {
            throw new EntityNotFoundException("member does not exist");
        }

        return new CustomUserDetails(member);
    }
}
