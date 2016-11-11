package com.pimp.repositories;

import com.pimp.domain.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDocument, String> {

    public UserDocument findByUserName(String userName);

    public UserDocument save(UserDocument user);
}

