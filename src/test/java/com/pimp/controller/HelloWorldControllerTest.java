package com.pimp.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HelloWorldControllerTest {

    MockMvc server;

    @Before
    public void setUp() throws Exception {
        server = MockMvcBuilders.standaloneSetup(new HelloWorldController()).build();
    }

    @Test
    public void testSayHello() throws Exception {
        server.perform(get("/hello")).andExpect(status().isOk());
    }
}