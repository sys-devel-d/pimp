package com.pimp.controller;

import com.pimp.commons.exceptions.EntitiesNotFoundException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.Group;
import com.pimp.services.GroupService;
import com.pimp.services.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

public abstract class GroupController<T extends Group> {

    private final GroupService<T> groupService;
    private final UserService userService;

    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @RequestMapping(method = GET, path="/user/{userName}")
    public List<T> getList(@PathVariable String userName) {
        return groupService.findByUserName(userName);
    }

    @RequestMapping(method = GET, path = "/{name}")
    public T get(@PathVariable String name) {
        return groupService.find(name);
    }

    @RequestMapping(method = POST)
    public void create(@Valid @RequestBody T group) {
        List<String> nonExistingUsers = group.getUserNames().stream()
                .filter(u -> !userService.exists(u))
                .collect(Collectors.toList());
        if (!nonExistingUsers.isEmpty()) {
            throw new EntitiesNotFoundException("The following users do not exist: " +
                nonExistingUsers.stream().reduce((s1, s2) -> s1 + ", " + s2).get()
            );
        }

        groupService.create(group);
    }

    @RequestMapping(method = DELETE, path = "/{name}")
    public void delete(@PathVariable String name) {
        groupService.delete(name);
    }

    @RequestMapping(method = PATCH, path = "/{groupName}/{userName}")
    public void add(@PathVariable String groupName, @PathVariable String userName) {
        if (!userService.exists(userName)) {
            throw new EntityNotFoundException("User " + userName + "does not exist.");
        }

        groupService.add(groupName, userName);
    }

    @RequestMapping(method = DELETE, path = "/{groupName}/{userName}")
    public void remove(@PathVariable String groupName, @PathVariable String userName) {
        if (!userService.exists(userName)) {
            throw new EntityNotFoundException("User " + userName + "does not exist.");
        }

        groupService.remove(groupName, userName);
    }
}
