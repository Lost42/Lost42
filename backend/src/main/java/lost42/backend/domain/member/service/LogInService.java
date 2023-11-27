package lost42.backend.domain.member.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.Response.SuccessResponse;
import lost42.backend.config.jwt.provider.TokenProvider;
import lost42.backend.domain.member.dto.LoginReq;
import lost42.backend.domain.member.entity.Member;
import lost42.backend.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@AllArgsConstructor
public class LogInService {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    // TODO 로그인 후 토큰 어떻게??
    public SuccessResponse login(LoginReq req) {
        Member member = memberRepository.findByEmail(req.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("일치하는 회원 없음"));

        String accessToken = tokenProvider.generateAccessToken(req.getUserEmail());
        String refreshToken = tokenProvider.generateRefreshToken(req.getUserEmail());

        return SuccessResponse.noContent();
    }
}
