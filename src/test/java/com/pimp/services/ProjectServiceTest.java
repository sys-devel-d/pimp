package com.pimp.services;

import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.Project;
import com.pimp.repositories.ProjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository repository;

    private ProjectService service;

    @Before
    public void setUp() throws Exception {
        service = new ProjectService(repository);
    }

    @Test
    public void testCreate() throws Exception {
        when(repository.save(any(Project.class))).thenReturn(new Project());

        Project project = new Project().setName("FooProject").setUserNames(Arrays.asList("Foo", "Bar"));

        service.create(project);

        verify(repository, atLeastOnce()).save(project);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDoesNotExist() throws Exception {
        when(repository.findByName(any())).thenReturn(null);

        service.find("something");
    }

    @Test
    public void testFindByUserName() throws Exception {
        when(repository.findByUserName("foo"))
                .thenReturn(Arrays.asList(
                        new Project()
                                .setName("FooProject")
                                .setUserNames(Arrays.asList("foo")),
                        new Project()
                                .setName("BarProject")
                                .setUserNames(Arrays.asList("foo"))
                        )
                );

        List<Project> projects = service.findByUserName("foo");

        assertThat(projects).hasSize(2);
        assertThat(projects).contains(
                new Project()
                        .setName("FooProject")
                        .setUserNames(Arrays.asList("foo")));
        assertThat(projects).contains(
                new Project()
                        .setName("BarProject")
                        .setUserNames(Arrays.asList("foo")));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNonExisting() throws Exception {
        when(repository.findByName(any())).thenReturn(null);

        service.delete("foo");
    }
}