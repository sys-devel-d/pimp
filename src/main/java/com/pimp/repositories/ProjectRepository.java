package com.pimp.repositories;

import com.pimp.domain.ProjectDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProjectRepository extends MongoRepository<ProjectDocument, String> {

    public ProjectDocument findByName(String name);

    public ProjectDocument save(ProjectDocument document);

    @Query("{userNames:{$elemMatch:{$eq: 'userName'}}}")
    public List<ProjectDocument> findByUserName(String userName);
}
