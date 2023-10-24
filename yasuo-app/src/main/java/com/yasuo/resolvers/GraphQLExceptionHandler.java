package com.yasuo.resolvers;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof AuthenticationException) {
            return constructGraphQLError(ErrorType.UNAUTHORIZED, ex, env);
        } else if (ex instanceof IllegalArgumentException) {
            return constructGraphQLError(ErrorType.BAD_REQUEST, ex, env);
        } else {
            return defaultGraphQLError(env);
        }
    }

    private GraphQLError constructGraphQLError(ErrorType errorType, Throwable ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .errorType(errorType)
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

    private GraphQLError defaultGraphQLError(DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .message("An error occurred while processing request.")
                .path(env.getExecutionStepInfo().getPath())
                .build();
    }
}
