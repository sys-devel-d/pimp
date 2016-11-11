package com.pimp.services;

import com.pimp.domain.ClientDocument;
import com.pimp.repositories.ClientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

@Service
public class CustomClientDetailsService implements ClientDetailsService {

    private ClientDetailsRepository repository;

    @Autowired
    public CustomClientDetailsService(ClientDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDocument clientDocument = repository.findByClientId(clientId);

        BaseClientDetails baseClientDetails = new BaseClientDetails(
                clientDocument.getClientId(),
                clientDocument.resourceIdsAsString(),
                clientDocument.scopesAsString(),
                clientDocument.grantTypesAsString(),
                clientDocument.authoritiesAsString());
        baseClientDetails.setClientSecret(clientDocument.getClientSecret());

        return baseClientDetails;
    }

    public void saveClientDetails(ClientDocument clientDocument) {

        repository.save(clientDocument);
    }
}
