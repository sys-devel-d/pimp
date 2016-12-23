package com.pimp.services;

import com.pimp.domain.User;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CustomTokenEnhancerServiceTest {

    private CustomTokenEnhancerService tokenEnhancer = new CustomTokenEnhancerService();

    @Test
    public void testEnhancesTokenWithUserName() throws Exception {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(DefaultOAuth2AccessToken.ACCESS_TOKEN);
        OAuth2Authentication authentication = Mockito.mock(OAuth2Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new User().setUserName("fooBar"));

        OAuth2AccessToken enhanced = tokenEnhancer.enhance(token, authentication);

        assertThat(enhanced.getAdditionalInformation().containsKey("user_name"));
        assertThat(enhanced.getAdditionalInformation().get("user_name")).isEqualTo("fooBar");
    }

    @Test
    public void testEnhanceWithAdminScope() throws Exception {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(DefaultOAuth2AccessToken.ACCESS_TOKEN);
        OAuth2Authentication authentication = Mockito.mock(OAuth2Authentication.class);
        when(authentication.getPrincipal()).thenReturn(
                new User()
                        .setUserName("fooBar")
                        .setRoles(Arrays.asList("ADMIN")));

        OAuth2AccessToken enhanced = tokenEnhancer.enhance(token, authentication);

        assertThat(enhanced.getScope())
                .containsAll((Arrays.asList("user_actions", "admin_actions")));
    }
}