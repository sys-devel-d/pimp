package com.pimp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

@Document
public class ClientDocument {

    @Id
    private String clientId;
    private String clientSecret;
    private List<String> resourceIds;
    private List<String> scopes;
    private List<String> grantTypes;
    private List<String> authorities;


    public String resourceIdsAsString() {
        return collectAsString(resourceIds);
    }


    public String grantTypesAsString() {
        return collectAsString(grantTypes);
    }

    public String scopesAsString() {
        return collectAsString(scopes);
    }

    public String authoritiesAsString() {
        return collectAsString(authorities);
    }

    private String collectAsString(List<String> raw) {
        return raw.stream().map(String::toString).collect(Collectors.joining(", "));
    }

    public String getClientId() {
        return clientId;
    }

    public ClientDocument setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public ClientDocument setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public ClientDocument setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
        return this;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public ClientDocument setScopes(List<String> scopes) {
        this.scopes = scopes;
        return this;
    }

    public List<String> getGrantTypes() {
        return grantTypes;
    }

    public ClientDocument setGrantTypes(List<String> grantTypes) {
        this.grantTypes = grantTypes;
        return this;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public ClientDocument setAuthorities(List<String> authorities) {
        this.authorities = authorities;
        return this;
    }


}
