package com.pimp.repositories;

import com.pimp.domain.ClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDetailsRepository extends MongoRepository<ClientDocument, String> {

    public ClientDocument findByClientId(String clientId);
}
