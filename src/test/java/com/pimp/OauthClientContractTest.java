package com.pimp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PimpRestApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OauthClientContractTest {

    @LocalServerPort
    private int port;

    private OAuth2RestTemplate template;

    @Before
    public void setUp() throws Exception {
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setAccessTokenUri("http://localhost:" + port + "/oauth/token");
        resourceDetails.setClientId("foo");
        resourceDetails.setClientSecret("secret");
        template = new OAuth2RestTemplate(resourceDetails);
    }

    @Test
    public void testName() throws Exception {
        template.getAccessToken();
    }

}
