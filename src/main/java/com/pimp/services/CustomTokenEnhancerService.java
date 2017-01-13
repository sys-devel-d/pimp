package com.pimp.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomTokenEnhancerService implements TokenEnhancer {

    public CustomTokenEnhancerService() {
        super();
    }

    @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (!(accessToken instanceof DefaultOAuth2AccessToken)) {
            throw new IllegalArgumentException("Token must be a DefaultOAuth2AccessToken");
        }
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        UserDetails user = (UserDetails) authentication.getPrincipal();
        Map<String, Object> additionalInformation = new HashMap<>();
        additionalInformation.put("user_name", user.getUsername());
        List<String> roles = user.getAuthorities()
          .stream()
          .map(GrantedAuthority::getAuthority)
          .collect(Collectors.toList());
        additionalInformation.put("user_roles", roles);
        token.setAdditionalInformation(additionalInformation);

        return token;
    }
}
