package com.simon.portfoliotracker.auth.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simon.portfoliotracker.wallet.Wallet;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@NoArgsConstructor
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private Wallet wallet;

    public ApplicationUser(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.wallet = new Wallet();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
