package com.pimp.controller;

import com.pimp.domain.Team;
import com.pimp.services.TeamService;
import com.pimp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/team")
@PreAuthorize("#oauth2.hasScope('user_actions')")
public class TeamController extends GroupController<Team> {

    @Autowired
    public TeamController(TeamService teamService, UserService userService) {
        super(teamService, userService);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Team create(@Valid @RequestBody Team group) {
        return super.create(group);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void delete(@PathVariable String key) {
        super.delete(key);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void add(@PathVariable String groupName, @PathVariable String userName) {
        super.add(groupName, userName);
    }

}
