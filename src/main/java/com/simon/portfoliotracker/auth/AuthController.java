package com.simon.portfoliotracker.auth;

import com.simon.portfoliotracker.auth.user.AppUserService;
import com.simon.portfoliotracker.auth.user.UserDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/users")
public class AuthController {

    private final AppUserService userService;

    public AuthController(AppUserService userService) {
        this.userService = userService;
    }
    @GetMapping("/login")
    public UserDTO login(Principal user){
        return userService.getUserDto(user.getName());
    }
}
