package lost42.backend.common.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.domain.member.dto.FindPasswordReq;
import lost42.backend.domain.member.exception.MemberErrorCode;
import lost42.backend.domain.member.exception.MemberErrorException;
import lost42.backend.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;

    public String sendMail(FindPasswordReq req) {
        memberRepository.findByEmail(req.getUserEmail())
                .orElseThrow(() -> new MemberErrorException(MemberErrorCode.INVALID_USER));

        EmailMessage emailMessage = EmailMessage.builder()
                .to(req.getUserEmail())
                .subject("Lost42 임시 비밀번호 발급")
                .build();

        String authCode = createCode();

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText("Lost42 : 인증코드는 " + authCode + " 입니다.", false);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.warn("Send mail error");
            throw new RuntimeException(e);
        }

        return authCode;
    }

    private String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) (random.nextInt(26) + 97)); break;
                case 1: key.append((char) (random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }
}
