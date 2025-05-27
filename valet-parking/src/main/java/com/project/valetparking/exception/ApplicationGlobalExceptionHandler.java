package com.project.valletparking.exception;

import com.project.valletparking.enums.InfoType;
import com.project.valletparking.model.ResponseMessage;
import com.project.valletparking.service.ResponseMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application
 */
@ControllerAdvice
public class ApplicationGlobalExceptionHandler {

    @Autowired
    private ResponseMessageService responseMessageService;

    /**
     * Handle custom exceptions
     */
    @ExceptionHandler(ApplicationCustomException.class)
    public ResponseEntity<ResponseMessage<Object>> handleCustomException(ApplicationCustomException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(responseMessageService.createResponse(null, ex.getInfoType(), ex.getMessage()));
    }

    /**
     * Handle validation exceptions
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseMessageService.createResponse(errors, InfoType.ERROR, "Validation failed"));
    }


    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage<Object>> handleGlobalException(Exception ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseMessageService.createResponse(null, InfoType.ERROR, "An unexpected error occurred: " + ex.getMessage()));
    }
}