package pl.feature.toggle.service.configuration.project.infrastructure.in.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.feature.toggle.service.configuration.project.domain.exception.CannotOperateOnArchivedProjectException;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectAlreadyExistsException;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectNotFoundException;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectUpdateFailedException;
import pl.feature.toggle.service.web.ErrorCode;
import pl.feature.toggle.service.web.ErrorResponse;
import pl.feature.toggle.service.web.correlation.CorrelationProvider;

import static pl.feature.toggle.service.web.ErrorCode.*;

@ControllerAdvice
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
