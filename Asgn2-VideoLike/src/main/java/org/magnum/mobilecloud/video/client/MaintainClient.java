package org.magnum.mobilecloud.video.client;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.magnum.mobilecloud.integration.test.UnsafeHttpsClient;
import org.magnum.mobilecloud.video.TestData;
import org.magnum.mobilecloud.video.repository.Video;
import retrofit.client.ApacheClient;

import java.io.IOException;

public class MaintainClient {

    private final String TEST_URL = "https://localhost:8443";

    private final String USERNAME1 = "admin";
    private final String USERNAME2 = "user0";
    private final String PASSWORD = "pass";
    private final String CLIENT_ID = "mobile";

    private static Video video;// = TestData.randomVideo();

    static ObjectMapper mapper = new ObjectMapper();

    private VideoSvcApi User1 = new SecuredRestBuilder()
            .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
            .setEndpoint(TEST_URL)
            .setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
                    // .setLogLevel(LogLevel.FULL)
            .setUsername(USERNAME1).setPassword(PASSWORD).setClientId(CLIENT_ID)
            .build().create(VideoSvcApi.class);

    public static void main(String[] args) throws IOException {

        video = TestData.randomVideo();
        System.out.println("hello video: " + mapper.writeValueAsString(video));
    }
}
