package com.pimp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PimpRestApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OauthClientContractTest {

    @LocalServerPort
    private int port;

    private OAuth2RestTemplate template;

    @Before
    public void setUp() throws Exception {
        ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
        details.setUsername("FOO");
        details.setPassword("BAR");
        details.setAccessTokenUri("http://localhost:" + port + "/api/oauth/token");
        details.setClientId("client");
        details.setClientSecret("secret");
        template = new OAuth2RestTemplate(details);
    }

    @Test
    public void testName() throws Exception {
        template.getAccessToken();
    }

}
