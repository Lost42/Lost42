package lost42.backend.common.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.redis.authToken.AuthToken;
import lost42.backend.common.redis.authToken.AuthTokenRepository;
import lost42.backend.domain.member.dto.FindPasswordReq;
import lost42.backend.domain.member.entity.Member;
import lost42.backend.domain.member.exception.MemberErrorCode;
import lost42.backend.domain.member.exception.MemberErrorException;
import lost42.backend.domain.member.repository.MemberRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;
    private final AuthTokenRepository authTokenRepository;

    public void sendMail(FindPasswordReq req) {
        Member member = memberRepository.findByEmail(req.getUserEmail())
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));

        EmailMessage emailMessage = EmailMessage.builder()
                .to(req.getUserEmail())
                .subject("Lost42 비밀번호 찾기")
                .build();

        String authUrl = createUrl(member);
        log.warn("authUrl: {}", authUrl);
        String emailContent = "<html><body>"
                + "<p>Lost42 : 인증 확인을 위해 버튼을 5분 이내로 눌러주세요. <a href='" + authUrl + "'>인증 확인</a></p>"
                + "</body></html>";

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.warn("Send mail error");
            throw new RuntimeException(e);
        }

    }

    public String createUrl(Member member) {
        String token = UUID.randomUUID().toString();
        String cleanedToken = token.replace("-", "");
        String authUrl = "http://localhost:8080/api/v1/members/auth?id=" + member.getMemberId() + "&token=" + cleanedToken;

        authTokenRepository.save(AuthToken.fromToken(member.getMemberId(), cleanedToken));
        return authUrl;
    }
}

