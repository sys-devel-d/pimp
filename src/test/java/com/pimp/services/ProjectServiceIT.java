package com.pimp.services;

import com.pimp.PimpRestApplication;
import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.config.MongoConfig;
import com.pimp.domain.Project;
import com.pimp.repositories.ProjectRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PimpRestApplication.class, MongoConfig.class, ProjectService.class, ProjectRepository.class},
        properties = "db.port=37017")
public class ProjectServiceIT {

    @Autowired
    ProjectService service;

    @Test(expected = EntityAlreadyExistsException.class)
    public void testDuplicate() throws Exception {
        Project project = new Project().setName("FooProject").setUserNames(Arrays.asList("Foo", "Bar"));

        service.createProject(project);
        service.createProject(project);
    }
}