package com.bestaone.springboot.oauth2.aurhserver.config;

import com.bestaone.springboot.oauth2.aurhserver.domain.Authority;
import com.bestaone.springboot.oauth2.aurhserver.domain.Role;
import com.bestaone.springboot.oauth2.aurhserver.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SimpleUserDetailsService implements UserDetailsService {

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService userService;

    @Autowired
    public RoleService roleService;

    @Autowired
    public AuthorityService authorityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.bestaone.springboot.oauth2.aurhserver.domain.User user = userService.findByUsername(username);

        Set<GrantedAuthority> authorities = new HashSet<>();

        List<Role> roles = roleService.findByRoleId(user.getId());
        for (Role role : roles){
            authorities.add(new SimpleGrantedAuthority(role.getCode()));
        }

        List<Authority> authList = authorityService.findByRoleId(user.getId());
        for (Authority auth : authList){
            authorities.add(new SimpleGrantedAuthority(auth.getCode()));
        }

        return new User(user.getUsername(),passwordEncoder.encode(user.getPassword()),authorities);
    }

}
