package com.yasuo.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.yasuo.dtos.authentication.AuthenticationResponse;
import com.yasuo.dtos.authentication.LoginRequest;
import com.yasuo.dtos.authentication.ResponseDto;
import com.yasuo.services.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@DgsComponent
@RequiredArgsConstructor
public class AuthDataFetcher {
    private final AuthenticationService authenticationService;

    @DgsMutation
    public AuthenticationResponse login(@InputArgument LoginRequest loginRequest) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return authenticationService.login(loginRequest);
    }

    @DgsMutation
    public AuthenticationResponse refreshToken(@InputArgument String userFingerprintHash) throws NoSuchAlgorithmException {
        return authenticationService.refreshToken(userFingerprintHash);
    }

    @DgsQuery
    public ResponseDto logout() {
        return authenticationService.logout();
    }
}
