package com.tpe.controller.User;

import com.tpe.payload.request.SigninRequest;
import com.tpe.payload.response.AuthResponse;
import com.tpe.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/signin")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    // http://localhost:8080/signin + POST + JSON
    @PostMapping
  // @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE','STAFF')")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody @Valid SigninRequest signinRequest) {
        return authenticationService.authenticateUser(signinRequest);
    }
}