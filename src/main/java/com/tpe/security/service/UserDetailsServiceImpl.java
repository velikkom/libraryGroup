package com.tpe.security.service;

/*
import com.tpe.entity.concretes.user.User;

import com.tpe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new UserDetailsImpl(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole().getRoleType().getName()
                    );
        } throw new UsernameNotFoundException("user email ' = " + email + " not found");


    }
}
*/
import com.tpe.entity.concretes.user.User;
import com.tpe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    //private final Logger log;
    private static final Logger log =  LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);

        }
        log.debug("User found with email: {}", email);
        // Ensure that the role names are prefixed with "ROLE_" to conform with Spring Security's default behavior
        String roleName = "ROLE_" + user.getRole().getRoleType().getName();

        // Create the authorities list containing the roles
        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

//        return new UserDetailsImpl(
//                user.getId(),
//                user.getEmail(),
//                user.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleType().getName())).toString()
//        );
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority) // Provide the correct authorities list
        );
    }
}