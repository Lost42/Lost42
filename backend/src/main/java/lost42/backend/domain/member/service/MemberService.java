package lost42.backend.domain.member.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.Response.SuccessResponse;
import lost42.backend.common.auth.dto.CustomUserDetails;
import lost42.backend.domain.member.dto.ChangeUserDataReq;
import lost42.backend.domain.member.dto.ResetPasswordReq;
import lost42.backend.domain.member.dto.UserInfoRes;
import lost42.backend.domain.member.entity.Member;
import lost42.backend.domain.member.exception.MemberErrorCode;
import lost42.backend.domain.member.exception.MemberErrorException;
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

    public UserInfoRes getUserInfo (CustomUserDetails securityUser) {
        Member member = memberRepository.findById(securityUser.getMemberId())
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));

        return UserInfoRes.fromMember(member);
    }

    public boolean resetPassword(String name, String email, ResetPasswordReq req) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));

        if (!name.equals(member.getName())) {
            throw new MemberErrorException(MemberErrorCode.INVALID_USER);
        }

        String newPassword = passwordEncoder.encode(req.getUserPassword());
        if (!isValidPassword(req.getUserPassword())) {
            throw new MemberErrorException(MemberErrorCode.INVALID_PASSWORD);
        }

        member.resetPassword(newPassword);
        memberRepository.save(member);

        return true;
    }
    public boolean changeUserData(ChangeUserDataReq req, CustomUserDetails securityUser) {
        Member member = memberRepository.findById(securityUser.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member X"));

        String changePassword = passwordEncoder.encode(req.getUserPassword());
        member.changePassword(changePassword);
        memberRepository.save(member);

        return true;
    }

    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,12}$";

        return Pattern.matches(regex, password);
    }

    public boolean withdrawalMember(CustomUserDetails securityUser) {
        Member member = memberRepository.findById(securityUser.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member X"));

        member.withdrawal();

        return true;
    }
}
