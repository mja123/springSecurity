package com.mja123.security.controller.exceptions;

import com.mja123.security.exceptions.NotFoundException;
import com.mja123.security.exceptions.NotUniqueAttributeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(NotUniqueAttributeException.class)
    public ResponseEntity<ErrorResponse> notUniqueAttributeHandler(NotUniqueAttributeException exception) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorTypes.NOT_UNIQUE.error, exception.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundHandler(NotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorTypes.NOT_FOUND.error, exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> argumentValidationHandler(MethodArgumentNotValidException exception) {
        List<ErrorResponse> validationErrors = new ArrayList<>();
        exception.getFieldErrors().forEach(e ->
                validationErrors.add(new ErrorResponse(ErrorTypes.ARGUMENT_VALIDATION.error,
                        Objects.requireNonNull(exception
                                .getFieldError()).getDefaultMessage()))
        );
        return ResponseEntity
                .badRequest()
                .body(validationErrors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> argumentTypeMisMatchHandler(MethodArgumentTypeMismatchException exception) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorTypes.ARGUMENT_MISMATCH.error, exception.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedHandler(AccessDeniedException exception) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorTypes.ACCESS_DENIED.error, "You do not have permission to access this resource");
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorTypes.UNKNOWN_ERROR.error, exception.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(errorResponse);
    }

}
