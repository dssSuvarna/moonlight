package com.builderbackend.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// this is used as a dummy endpoint

@RestController
public class WelcomeController {
    
    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome!";
    }

}
