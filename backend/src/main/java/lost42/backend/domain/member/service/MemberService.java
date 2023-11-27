package lost42.backend.domain.member.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.Response.SuccessResponse;
import lost42.backend.domain.member.dto.ResetPasswordReq;
import lost42.backend.domain.member.entity.Member;
import lost42.backend.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.regex.Pattern;

@Service
@Slf4j
@AllArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // TODO 추후 공통 응답 핸들러로 변경
    public SuccessResponse reSetPassword(String name, String email, ResetPasswordReq req) {
        log.warn("Reset password, user: {}, email: {}", name, email);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));

        if (!name.equals(member.getName())) {
            throw new IllegalArgumentException("유저가아님");
        }

        String newPassword = passwordEncoder.encode(req.getUserPassword());
        if (!isValidPassword(req.getUserPassword())) {
            throw new IllegalArgumentException("비밀번호 정책에 어긋남");
        }

        member.resetPassword(newPassword);
        memberRepository.save(member);

        return SuccessResponse.noContent();
    }

    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,12}$";

        return Pattern.matches(regex, password);
    }
}
