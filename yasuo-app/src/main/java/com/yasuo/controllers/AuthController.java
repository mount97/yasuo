package com.yasuo.controllers;

import com.yasuo.dtos.authentication.AuthenticationResponse;
import com.yasuo.dtos.authentication.LoginRequest;
import com.yasuo.dtos.authentication.ResponseDto;
import com.yasuo.services.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @MutationMapping(value = "login")
    public AuthenticationResponse login(@Argument LoginRequest loginRequest) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return authenticationService.login(loginRequest);
    }

    @QueryMapping(value = "refreshToken")
    public AuthenticationResponse refreshToken(@Argument String userFingerprintHash) throws NoSuchAlgorithmException {
        return authenticationService.refreshToken(userFingerprintHash);
    }

    @MutationMapping(value = "logout")
    public ResponseDto logout() {
        return authenticationService.logout();
    }
}
