package com.pimp.services;

import com.pimp.domain.Project;
import com.pimp.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService extends GroupService<Project> {

    @Autowired
    public ProjectService(ProjectRepository repository) {
        super(repository);
    }

    @Override
    protected String getGroupType() {
        return "Project";
    }
}
