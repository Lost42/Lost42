package lost42.backend.common.redis.authToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.domain.member.exception.MemberErrorCode;
import lost42.backend.domain.member.exception.MemberErrorException;
import lost42.backend.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthTokenService {
    private final MemberRepository memberRepository;
    private final AuthTokenRepository authTokenRepository;

    public boolean validate(Long memberId, String token) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));

        AuthToken check = authTokenRepository.findById(memberId)
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.AUTH_TOKEN_TIME_OUT));

        if (!check.getToken().equals(token)) {
            throw new MemberErrorException(MemberErrorCode.INVALID_TOKEN_DATA);
        }

        return true;
    }
}
