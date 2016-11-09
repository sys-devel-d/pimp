package com.pimp.services;

import com.pimp.domain.ClientDocument;
import com.pimp.repositories.ClientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class MongoClientDetailsService implements ClientDetailsService {

    @Autowired
    private ClientDetailsRepository repository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDocument clientDocument = repository.findByClientId(clientId);

        return new BaseClientDetails(
                clientDocument.getClientId(),
                clientDocument.resourceIdsAsString(),
                clientDocument.scopesAsString(),
                clientDocument.grantTypesAsString(),
                clientDocument.authoritiesAsString());
    }
}
