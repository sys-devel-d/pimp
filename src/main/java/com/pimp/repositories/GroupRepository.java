package com.pimp.repositories;

import com.pimp.domain.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GroupRepository<T extends Group> extends MongoRepository<T, String> {

    public T findByName(String name);

    @Query("{userNames:{$elemMatch:{$eq: ?0}}}")
    public List<T> findByUserName(String userName);
}
