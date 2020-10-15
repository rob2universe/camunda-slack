package org.camunda.bpm.example.slack;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

@Slf4j
public class SlackClientTest {

    public static Properties properties;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        properties = new Properties();
        InputStream propertiesFile = SlackClientTest.class.getClassLoader().getResourceAsStream("application.properties");
        properties.load(propertiesFile);
    }

    @Test
    public void testPostMessage() {

        SlackClient slackClient = new SlackClient(properties.getProperty("slack.bot.oauth.token"), properties.getProperty("slack.bot.channel"));
        String response = slackClient.sendToChannel("Hello from Camunda at " + LocalDateTime.now());
        log.info("response: {}", response);
        assertTrue(response.contains("warning=null, error=null, needed=null"));
    }
}
