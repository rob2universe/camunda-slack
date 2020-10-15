package org.camunda.bpm.example.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class SlackClient {

    private static String SLACK_BOT_OAUT_TOKEN;
    private static String SLACK_CHANNEL;

    public SlackClient() {
    }

    @Autowired
    public SlackClient(@Value("${slack.bot.oauth.token}") String token, @Value("${slack.bot.channel}") String channel) {
        SLACK_BOT_OAUT_TOKEN = token;
        SLACK_CHANNEL = channel;
        log.debug("Slack OAUTH Token initialized {}", token);
        log.debug("Slack channel initialized {}", channel);
    }

    /*  Slack documentation
     *      https://api.slack.com/start/overview
     *      https://slack.dev/java-slack-sdk/guides/web-api-basics
     *
     *  Ensure Bot Token Scopes have been configured (chat:write, chat:write.public)
     *  and app has been installed to workspace
     */

    public String sendToChannel(String text) {
        if (null == SLACK_BOT_OAUT_TOKEN) {
            log.error("SLACK_BOT_OAUT_TOKEN is null !");
            throw new BeanInitializationException("Property slack.bot.oauth.token is not set.");
        }
        if (null == SLACK_CHANNEL) {
            log.error("SLACK_CHANNEL is null !");
            throw new BeanInitializationException("Property slack.bot.channel is not set.");
        }

        ChatPostMessageResponse response;
        try {
            Slack slack = Slack.getInstance();
            MethodsClient methods = slack.methods(SLACK_BOT_OAUT_TOKEN);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(SLACK_CHANNEL).text(text).build();
            response = methods.chatPostMessage(request);
            log.debug("response: {}", response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return e.getLocalizedMessage();
        } catch (SlackApiException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return e.getMessage();
        }
        if (null != response.getError()) {
            // could be e.g. missing_scope, not_authed, channel_not_found
            log.error("Slack returned error: {}", response.getError());
            return response.getError();
        } else
            return response.toString();
    }
}
