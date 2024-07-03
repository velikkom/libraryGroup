package com.tpe.security.service;

import com.tpe.entity.concretes.user.User;

import com.tpe.payload.messages.ErrorMessages;
import com.tpe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = (User) userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email"));

        Set<GrantedAuthority> authorities = user.getRole().getUsers()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getEmail()))
                .collect(Collectors.toSet());

        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword(), authorities.toString());
    }

   /* @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

//        User user = userRepository.findByEmail(email);
//        if (user != null) {
//            return new UserDetailsImpl(
//                    user.getId(),
//                    user.getEmail(),
//                    user.getPassword(),
//                    user.getRole().getRoleType().getName()
//                    );
//        } throw new UsernameNotFoundException("user email ' = " + email + " not found");
        User user = null;
        try {
            user = (User) userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        Set<GrantedAuthority> authorites = user.getRole().getUsers()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getEmail()))
                .collect(Collectors.toSet());
        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword(), authorites.toString());


    }*/
}
