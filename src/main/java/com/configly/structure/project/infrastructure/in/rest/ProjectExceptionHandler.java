package com.configly.structure.project.infrastructure.in.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.configly.structure.project.domain.exception.CannotOperateOnArchivedProjectException;
import com.configly.structure.project.domain.exception.ProjectAlreadyExistsException;
import com.configly.structure.project.domain.exception.ProjectNotFoundException;
import com.configly.structure.project.domain.exception.ProjectUpdateFailedException;
import com.configly.web.ErrorCode;
import com.configly.web.ErrorResponse;
import com.configly.web.correlation.CorrelationProvider;

import static com.configly.web.ErrorCode.*;

@RestControllerAdvice
@AllArgsConstructor
class ProjectExceptionHandler {

    private final CorrelationProvider correlationProvider;

    @ExceptionHandler(ProjectUpdateFailedException.class)
    ResponseEntity<ErrorResponse> handle(ProjectUpdateFailedException exception) {
        var errorResponse = createErrorResponse(PROJECT_WAS_MODIFIED_BY_ANOTHER_REQUEST, exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    ResponseEntity<ErrorResponse> handle(ProjectNotFoundException exception) {
        var errorResponse = createErrorResponse(PROJECT_NOT_FOUND, exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(ProjectAlreadyExistsException.class)
    ResponseEntity<ErrorResponse> handle(ProjectAlreadyExistsException exception) {
        var errorResponse = createErrorResponse(PROJECT_ALREADY_EXISTS, exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(CannotOperateOnArchivedProjectException.class)
    ResponseEntity<ErrorResponse> handle(CannotOperateOnArchivedProjectException exception) {
        var errorResponse = createErrorResponse(PROJECT_ARCHIVED, exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    private ErrorResponse createErrorResponse(ErrorCode errorCode, Exception e) {
        return ErrorResponse.from(errorCode, e, correlationProvider.current());
    }
}
