package com.configly.structure.environment.infrastructure.in.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.configly.structure.environment.domain.exception.*;
import com.configly.web.ErrorCode;
import com.configly.web.ErrorResponse;
import com.configly.web.correlation.CorrelationProvider;

import static com.configly.web.ErrorCode.*;

@RestControllerAdvice
@AllArgsConstructor
class EnvironmentExceptionHandler {

    private final CorrelationProvider correlationProvider;

    @ExceptionHandler(EnvironmentUpdateFailedException.class)
    ResponseEntity<ErrorResponse> handle(EnvironmentUpdateFailedException exception) {
        var errorResponse = createErrorResponse(ENVIRONMENT_WAS_MODIFIED_BY_ANOTHER_REQUEST, exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(EnvironmentNotFoundException.class)
    ResponseEntity<ErrorResponse> handle(EnvironmentNotFoundException exception) {
        var errorResponse = createErrorResponse(ENVIRONMENT_NOT_FOUND, exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(EnvironmentNotAssignedToProjectException.class)
    ResponseEntity<ErrorResponse> handle(EnvironmentNotAssignedToProjectException exception) {
        var errorResponse = createErrorResponse(ENVIRONMENT_NOT_ASSIGNED_TO_PROJECT, exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(EnvironmentAlreadyExistsException.class)
    ResponseEntity<ErrorResponse> handle(EnvironmentAlreadyExistsException exception) {
        var errorResponse = createErrorResponse(ENVIRONMENT_ALREADY_EXISTS, exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(CannotOperateOnArchivedEnvironmentException.class)
    ResponseEntity<ErrorResponse> handle(CannotOperateOnArchivedEnvironmentException exception) {
        var errorResponse = createErrorResponse(ENVIRONMENT_ARCHIVED, exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(CannotOperateOnEnvironmentForArchivedProjectException.class)
    ResponseEntity<ErrorResponse> handle(CannotOperateOnEnvironmentForArchivedProjectException exception) {
        var errorResponse = createErrorResponse(PROJECT_ARCHIVED, exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(CannotCreateEnvironmentForMissingProjectException.class)
    ResponseEntity<ErrorResponse> handle(CannotCreateEnvironmentForMissingProjectException exception) {
        var errorResponse = createErrorResponse(PROJECT_NOT_FOUND, exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    private ErrorResponse createErrorResponse(ErrorCode errorCode, Exception e) {
        return ErrorResponse.from(errorCode, e, correlationProvider.current());
    }

}
