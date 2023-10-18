package com.yasuo.services.auth;

import com.yasuo.dtos.authentication.AuthenticationResponse;
import com.yasuo.dtos.authentication.LoginRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationService {

    //JwtAuthenticationResponse register(LoginRequest request);
    AuthenticationResponse login(LoginRequest request);

    AuthenticationResponse refreshToken(String refreshToken);
}
