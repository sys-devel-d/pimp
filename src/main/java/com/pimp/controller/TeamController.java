package com.pimp.controller;

import com.pimp.domain.Team;
import com.pimp.services.TeamService;
import com.pimp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/team")
@PreAuthorize("#oauth2.hasScope('user_actions')")
public class TeamController extends GroupController<Team> {

    @Autowired
    public TeamController(TeamService teamService, UserService userService) {
        super(teamService, userService);
    }
}
