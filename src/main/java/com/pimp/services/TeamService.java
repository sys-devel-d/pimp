package com.pimp.services;

import com.pimp.domain.Team;
import com.pimp.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TeamService extends GroupService<Team> {

    @Autowired
    public TeamService(@Qualifier("teamRepository") GroupRepository repository) {
        super(repository);
    }

    @Override
    protected String getGroupType() {
        return "Team";
    }
}
