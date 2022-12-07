package com.simon.portfoliotracker.auth;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/users")
public class AuthController {

    @GetMapping("/login")
    public Principal login(Principal user){
        return user;
    }
}
