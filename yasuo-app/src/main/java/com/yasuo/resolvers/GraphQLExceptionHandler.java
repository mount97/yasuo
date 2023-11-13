package com.yasuo.resolvers;

import com.netflix.graphql.types.errors.ErrorType;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.yasuo.constants.AuthConstants.*;

@Component
public class GraphQLExceptionHandler implements DataFetcherExceptionHandler {

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable throwable = handlerParameters.getException();
        if (throwable instanceof AuthenticationException) {
            return constructResult(constructGraphQLError(BAD_CREDENTIALS_ERROR_MESSAGE, ErrorType.UNAUTHENTICATED, throwable, handlerParameters));
        } else if (throwable instanceof AccessDeniedException) {
            if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                return constructResult(constructGraphQLError(UNAUTHORIZED_ERROR_MESSAGE, ErrorType.UNAUTHENTICATED, throwable, handlerParameters));
            }
            return constructResult(constructGraphQLError(null, ErrorType.PERMISSION_DENIED, throwable, handlerParameters));
        } else if (throwable instanceof IllegalArgumentException) {
            return constructResult(constructGraphQLError(null, ErrorType.BAD_REQUEST, throwable, handlerParameters));
        } else {
            return constructResult(constructDefaultGraphQLError(handlerParameters));
        }
    }

    private GraphQLError constructGraphQLError(String customErrorMessage, ErrorType errorType, Throwable
            ex, DataFetcherExceptionHandlerParameters handlerParameters) {
        return TypedGraphQLError.newBuilder()
                .errorType(errorType)
                .message(StringUtils.isNotEmpty(customErrorMessage) ? customErrorMessage : ex.getMessage())
                .path(handlerParameters.getPath())
                .location(handlerParameters.getSourceLocation())
                .build();
    }

    private DataFetcherExceptionHandlerResult constructExceptionHandlerResult(GraphQLError graphQLError) {
        return DataFetcherExceptionHandlerResult.newResult()
                .error(graphQLError)
                .build();
    }

    private GraphQLError constructDefaultGraphQLError(DataFetcherExceptionHandlerParameters handlerParameters) {
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.INTERNAL)
                .message(COMMON_ERROR_MESSAGE)
                .path(handlerParameters.getPath())
                .location(handlerParameters.getSourceLocation())
                .build();
    }

    private CompletableFuture<DataFetcherExceptionHandlerResult> constructResult(GraphQLError error) {
        return CompletableFuture.completedFuture(constructExceptionHandlerResult(error));
    }
}
