package com.yasuo.controllers;

import com.yasuo.dtos.authentication.AuthenticationResponse;
import com.yasuo.dtos.authentication.LoginRequest;
import com.yasuo.services.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final HttpServletRequest httpServletRequest;

    @MutationMapping(value = "login")
    public AuthenticationResponse login(@Argument LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @QueryMapping(value = "refreshToken")
    public AuthenticationResponse refreshToken() {
        return authenticationService.refreshToken(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION));
    }
}
