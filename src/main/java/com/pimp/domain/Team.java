package com.pimp.domain;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Team {

    @Id
    private String teamName;
    private List<String> users;
}
