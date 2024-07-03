package com.tpe.service;

import com.tpe.payload.request.SigninRequest;
import com.tpe.payload.response.AuthResponse;
import com.tpe.repository.UserRepository;
import com.tpe.security.jwt.JwtUtils;
import com.tpe.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    public final JwtUtils jwtUtils;
    public final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // Not: Signin() *************************************************
    public ResponseEntity<AuthResponse> authenticateUser(SigninRequest signinRequest) {
        //! Username and password information is extracted from the incoming request
        String email = signinRequest.getEmail();
        String password = signinRequest.getPassword();
        // validating the user through the authenticationManager
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        //  Validated user is thrown into Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // JWT token created
        String token = "Bearer " + jwtUtils.generateJwtToken(authentication);
        // sign in user
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // !!! We will return the user who made the sign in transaction as a response, the necessary fields are set
        // !!! The GrantedAuthority type role structure is converted to String type
        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        Optional<String> role = roles.stream().findFirst();
        // Here, since we are sending the user information that performed the sign in process as a response, the necessary information is setting.
        AuthResponse.AuthResponseBuilder authResponse = AuthResponse.builder();
        authResponse.email(userDetails.getEmail());
        authResponse.token(token.substring(7));

        // !!!If there is role information, the variable in the response object is setting.
        role.ifPresent(authResponse::role);

        // !!! AuthResponse obj retuning ResponseEntity obj
        return ResponseEntity.ok(authResponse.build());
    }

}