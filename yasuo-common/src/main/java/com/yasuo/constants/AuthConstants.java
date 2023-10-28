package com.yasuo.constants;

public class AuthConstants {
    private AuthConstants() {
    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String UNAUTHORIZED_ERROR_MESSAGE = "Full authentication is required to access this resource";
    public static final String COMMON_ERROR_MESSAGE = "An error occurred while processing request";
    public static final String BAD_CREDENTIALS_ERROR_MESSAGE = "Invalid username or password";
}
