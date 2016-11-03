package com.pimp.repositories;

import com.pimp.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {

    public User findByUserName(String userName);

    public User save(User user);
}

