package com.pimp.repositories;

import com.pimp.domain.ClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientDetailsRepository extends MongoRepository<ClientDocument, String> {

    public ClientDocument findByClientId(String clientId);

    public ClientDocument save(ClientDocument clientDocument);
}
