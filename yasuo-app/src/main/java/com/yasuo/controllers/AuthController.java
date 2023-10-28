package com.yasuo.controllers;

import com.yasuo.dtos.authentication.AuthenticationResponse;
import com.yasuo.dtos.authentication.LoginRequest;
import com.yasuo.services.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @MutationMapping(value = "login")
    public AuthenticationResponse login(@Argument LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @QueryMapping(value = "refreshToken")
    public AuthenticationResponse refreshToken(@Argument String refreshToken) {
        return authenticationService.refreshToken(refreshToken);
    }
}
