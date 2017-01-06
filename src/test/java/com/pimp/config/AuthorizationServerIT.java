package com.pimp.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;

import static com.pimp.IntegrationTestAuthorizationTools.createReadyOAuth2Template;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "db.port=37017")
public class AuthorizationServerIT {

    @LocalServerPort
    private int port;
    private OAuth2RestTemplate restTemplate;


    @Before
    public void setUp() throws Exception {
      restTemplate = createReadyOAuth2Template(port);
    }

  @Test
    public void testObtainAccessToken() throws Exception {
        OAuth2AccessToken accessToken = restTemplate.getAccessToken();

        assertThat(accessToken).isNotNull();
    }
}
