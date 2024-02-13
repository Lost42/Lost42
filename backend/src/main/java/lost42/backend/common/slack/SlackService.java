package lost42.backend.common.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SlackService {
    @Value("${slack.token}")
    private String slackToken;
    @Value("${slack.channel}")
    private String slackChannel;

    public void sendSlackMessage(String message) {
        try {
            MethodsClient methods = Slack.getInstance().methods(slackToken);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(slackChannel)
                    .text(message)
                    .build();

            methods.chatPostMessage(request);

            log.info("Slack {}에 메세지 등록 완료", slackChannel);

        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }
}
