package com.yasuo.services.auth;

import com.yasuo.dtos.authentication.AuthenticationResponse;
import com.yasuo.dtos.authentication.LoginRequest;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Repository
public interface AuthenticationService {

    //JwtAuthenticationResponse register(LoginRequest request);
    AuthenticationResponse login(LoginRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException;

    AuthenticationResponse refreshToken(String userFingerprintHash) throws NoSuchAlgorithmException;
}
