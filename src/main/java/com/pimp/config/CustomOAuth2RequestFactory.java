package com.pimp.config;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CustomOAuth2RequestFactory implements OAuth2RequestFactory {

  private final ClientDetailsService clientDetailsService;

  private SecurityContextAccessor securityContextAccessor = new DefaultSecurityContextAccessor();

  private boolean checkUserScopes = false;

  public CustomOAuth2RequestFactory(ClientDetailsService clientDetailsService) {
    this.clientDetailsService = clientDetailsService;
  }

  public void setSecurityContextAccessor(SecurityContextAccessor securityContextAccessor) {
    this.securityContextAccessor = securityContextAccessor;
  }

  public void setCheckUserScopes(boolean checkUserScopes) {
    this.checkUserScopes = checkUserScopes;
  }

  public AuthorizationRequest createAuthorizationRequest(Map<String, String> authorizationParameters) {

    String clientId = authorizationParameters.get(OAuth2Utils.CLIENT_ID);
    String state = authorizationParameters.get(OAuth2Utils.STATE);
    String redirectUri = authorizationParameters.get(OAuth2Utils.REDIRECT_URI);
    Set<String> responseTypes = OAuth2Utils.parseParameterList(authorizationParameters
      .get(OAuth2Utils.RESPONSE_TYPE));

    Set<String> scopes = extractScopes(authorizationParameters, clientId);

    AuthorizationRequest request = new AuthorizationRequest(authorizationParameters,
      Collections.<String, String> emptyMap(), clientId, scopes, null, null, false, state, redirectUri,
      responseTypes);

    ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
    request.setResourceIdsAndAuthoritiesFromClientDetails(clientDetails);

    return request;

  }

  public OAuth2Request createOAuth2Request(AuthorizationRequest request) {
    return request.createOAuth2Request();
  }

  public TokenRequest createTokenRequest(Map<String, String> requestParameters, ClientDetails authenticatedClient) {

    String clientId = requestParameters.get(OAuth2Utils.CLIENT_ID);
    if (clientId == null) {
      clientId = authenticatedClient.getClientId();
    }
    else {
      if (!clientId.equals(authenticatedClient.getClientId())) {
        throw new InvalidClientException("Given client ID does not match authenticated client");
      }
    }
    String grantType = requestParameters.get(OAuth2Utils.GRANT_TYPE);

    Set<String> scopes = extractScopes(requestParameters, clientId);
    TokenRequest tokenRequest = new TokenRequest(requestParameters, clientId, scopes, grantType);

    return tokenRequest;
  }

  public TokenRequest createTokenRequest(AuthorizationRequest authorizationRequest, String grantType) {
    TokenRequest tokenRequest = new TokenRequest(authorizationRequest.getRequestParameters(),
      authorizationRequest.getClientId(), authorizationRequest.getScope(), grantType);
    return tokenRequest;
  }

  public OAuth2Request createOAuth2Request(ClientDetails client, TokenRequest tokenRequest) {
    return tokenRequest.createOAuth2Request(client);
  }

  private Set<String> extractScopes(Map<String, String> requestParameters, String clientId) {
    Set<String> scopes = OAuth2Utils.parseParameterList(requestParameters.get(OAuth2Utils.SCOPE));
    ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

    if ((scopes == null || scopes.isEmpty())) {
      scopes = clientDetails.getScope();
    }

    if (checkUserScopes) {
      scopes = checkUserScopes(scopes, clientDetails);
    }
    return scopes;
  }

  private Set<String> checkUserScopes(Set<String> scopes, ClientDetails clientDetails) {
    if (!securityContextAccessor.isUser()) {
      return scopes;
    }
    Set<String> result = new LinkedHashSet<String>();
    Set<String> authorities = AuthorityUtils.authorityListToSet(securityContextAccessor.getAuthorities());
    for (String scope : scopes) {
      if (authorities.contains(scope) || authorities.contains(scope.toUpperCase())
        || authorities.stream().anyMatch(authority -> matchesScope(authority, scope))) {
        result.add(scope);
      }
    }
    return result;
  }

  @VisibleForTesting
  public static boolean matchesScope(String authority, String scope) {
    return authority.toLowerCase().concat("_actions").equals(scope);
  }

}
