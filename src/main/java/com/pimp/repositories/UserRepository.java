package com.pimp.repositories;

import com.pimp.domain.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<UserDocument, String> {

    public UserDocument findByUserName(String userName);

    public UserDocument save(UserDocument user);

    public UserDocument findByEmail(String email);

    public List<UserDocument> findAll();

}

