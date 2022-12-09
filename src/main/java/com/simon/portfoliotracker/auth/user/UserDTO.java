package com.simon.portfoliotracker.auth.user;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String role;
    private Long walletId;
}
