package com.pimp.config;

import com.pimp.services.CustomClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class OAuthConfig {

    @Configuration
    @EnableResourceServer
    protected static class ResourceSerConfig extends ResourceServerConfigurerAdapter {

        private final String RESOURCE_ID = "PIMP_RESOURCE";

        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.tokenStore(tokenStore)
                    .resourceId(RESOURCE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().authenticated();
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Autowired
        @Qualifier(value = "authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private CustomClientDetailsService clientDetailsService;

        @Autowired
        private AuthorizationServerTokenServices authorizationServerTokenServices;

        @Autowired
        private TokenEnhancer tokenEnhancer;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(clientDetailsService);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            DefaultOAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);

            endpoints
                    .tokenStore(tokenStore)
                    .authenticationManager(authenticationManager)
                    .tokenServices(authorizationServerTokenServices)
                    .requestFactory(requestFactory)
                    .tokenEnhancer(tokenEnhancer)

                    .tokenGranter(new ResourceOwnerPasswordTokenGranter(
                            authenticationManager,
                            authorizationServerTokenServices,
                            clientDetailsService,
                            requestFactory
                    ));
        }
    }


    @Configuration
    public class TokenServicesConfig {

        @Bean
        public TokenStore tokenStore() {
            return new InMemoryTokenStore();
        }

        @Bean
        AccessTokenProvider accessTokenProvider() {
            ResourceOwnerPasswordAccessTokenProvider tokenProvider = new ResourceOwnerPasswordAccessTokenProvider();
            return tokenProvider;
        }

        @Bean
        @Primary
        @Autowired
        AuthorizationServerTokenServices authorizationServerTokenServices(TokenStore tokenStore, TokenEnhancer tokenEnhancer) {
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setTokenStore(tokenStore);
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setTokenEnhancer(tokenEnhancer);
            return tokenServices;
        }
    }
}
