package com.pimp.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "db.port=37017")
public class AuthorizationServerIT {

    @LocalServerPort
    private int port;
    private OAuth2RestTemplate restTemplate;
    private ResourceOwnerPasswordResourceDetails details;

    @Before
    public void setUp() throws Exception {
        details = new ResourceOwnerPasswordResourceDetails();
        details.setUsername("test");
        details.setPassword("12345678");
        details.setClientId("angularClient");
        details.setClientSecret("secret123");
        details.setAccessTokenUri("http://localhost:" + port + "/oauth/token");
        restTemplate = new OAuth2RestTemplate(details);
    }

    @Test
    public void testObtainAccessToken() throws Exception {
        OAuth2AccessToken accessToken = restTemplate.getAccessToken();

        assertThat(accessToken).isNotNull();
    }
}