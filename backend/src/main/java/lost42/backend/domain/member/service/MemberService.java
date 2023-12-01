package lost42.backend.domain.member.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.Response.SuccessResponse;
import lost42.backend.config.auth.dto.CustomUserDetails;
import lost42.backend.domain.member.dto.ChangeUserDataReq;
import lost42.backend.domain.member.dto.ResetPasswordReq;
import lost42.backend.domain.member.dto.UserInfoRes;
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

    public SuccessResponse getUserInfo (CustomUserDetails securityUser) {
        Member member = memberRepository.findById(securityUser.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("회원 없음"));


        return SuccessResponse.from(UserInfoRes.fromMember(member));
    }

    // TODO 추후 공통 응답 핸들러로 변경
    public SuccessResponse resetPassword(String name, String email, ResetPasswordReq req) {
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
    public SuccessResponse changeUserData(ChangeUserDataReq req, CustomUserDetails securityUser) {
        Member member = memberRepository.findById(securityUser.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member X"));

        String changePassword = passwordEncoder.encode(req.getUserPassword());
        member.changePassword(changePassword);
        memberRepository.save(member);

        return SuccessResponse.noContent();
    }

    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,12}$";

        return Pattern.matches(regex, password);
    }

}
