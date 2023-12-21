package lost42.backend.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.Response.FailureResponse;
import lost42.backend.common.Response.SuccessResponse;
import lost42.backend.common.auth.MemberRole;
import lost42.backend.domain.member.dto.SignUpReq;
import lost42.backend.domain.member.dto.SignUpRes;
import lost42.backend.domain.member.entity.Member;
import lost42.backend.domain.member.exception.MemberErrorCode;
import lost42.backend.domain.member.exception.MemberErrorException;
import lost42.backend.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignUpService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    /**
     * 패스워드 정책 : 8~12자/ 알파벳 + 숫자 + 특수기호 최소 1개
     */
    public SignUpRes SignUp(SignUpReq req) {
        if (!checkDuplicateEmail(req.getUserEmail())) {
            throw new MemberErrorException(MemberErrorCode.ALREADY_EXIST_USER);
        }

        if (!isValidPassword(req.getUserPassword())) {
            throw new MemberErrorException(MemberErrorCode.INVALID_PASSWORD);
        }

        if (!req.getUserPassword().equals(req.getUserSecondPassword())) {
            throw new MemberErrorException(MemberErrorCode.MISS_MATCH_PASSWORD);
        }
        String encodePassword = passwordEncoder.encode(req.getUserPassword());

        Member newMember = Member.builder()
                .email(req.getUserEmail())
                .name(extractUsername(req.getUserEmail()))
                .password(encodePassword)
                .role(MemberRole.MEMBER)
                .build();

        memberRepository.save(newMember);
        return SignUpRes.from(newMember);
    }

    public boolean checkDuplicateEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member != null) {
            return false;
        }

        return true;
    }

    /**
     * 패스워드 검증
     */
    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,12}$";

        return Pattern.matches(regex, password);
    }

    /**
     * 유저 이름 떼내기
     */
    public static String extractUsername(String email) {
        int index = email.indexOf("@");

        return email.substring(0, index);
    }
}
