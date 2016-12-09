package com.pimp.repositories;

import com.pimp.domain.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {

    public Project findByName(String name);

    @Query("{userNames:{$elemMatch:{$eq: ?0}}}")
    public List<Project> findByUserName(String userName);
}
