package com.yasuo.resolvers;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.yasuo.constants.AuthConstants.COMMON_ERROR_MESSAGE;
import static com.yasuo.constants.AuthConstants.UNAUTHORIZED_ERROR_MESSAGE;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof AuthenticationException) {
            return constructGraphQLError(UNAUTHORIZED_ERROR_MESSAGE, ErrorType.UNAUTHORIZED, ex, env);
        } else if (ex instanceof AccessDeniedException) {
            if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                return constructGraphQLError(UNAUTHORIZED_ERROR_MESSAGE, ErrorType.UNAUTHORIZED, ex, env);
            }
            return constructGraphQLError(null, ErrorType.FORBIDDEN, ex, env);
        } else if (ex instanceof IllegalArgumentException) {
            return constructGraphQLError(null, ErrorType.BAD_REQUEST, ex, env);
        } else {
            return defaultGraphQLError(env);
        }
    }

    private GraphQLError constructGraphQLError(String customErrorMessage, ErrorType errorType, Throwable
            ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .errorType(errorType)
                .message(StringUtils.isNotEmpty(customErrorMessage) ? customErrorMessage : ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

    private GraphQLError defaultGraphQLError(DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .message(COMMON_ERROR_MESSAGE)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }
}
