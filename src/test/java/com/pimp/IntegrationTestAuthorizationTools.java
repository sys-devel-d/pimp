package com.pimp;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

public final class IntegrationTestAuthorizationTools {

  private IntegrationTestAuthorizationTools() {}

  public static OAuth2RestTemplate createReadyOAuth2Template(int port) {
    ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
    details.setUsername("test");
    details.setPassword("12345678");
    details.setClientId("angularClient");
    details.setClientSecret("secret123");
    details.setAccessTokenUri("http://localhost:" + port + "/oauth/token");

    return new OAuth2RestTemplate(details);
  }

}
