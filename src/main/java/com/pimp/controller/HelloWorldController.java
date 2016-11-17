package com.pimp.controller;

import com.pimp.domain.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path = "/hello")
public class HelloWorldController {

    @RequestMapping(method = GET)
    public Message sayHello() {
        return new Message("Hello, world.");
    }
}
