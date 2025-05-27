package com.project.valletparking.exception;

import com.project.valletparking.enums.InfoType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base custom exception class
 */
@Getter
public class ApplicationCustomException extends RuntimeException {
    private final HttpStatus status;
    private final InfoType infoType;

    public ApplicationCustomException(String message, HttpStatus status, InfoType infoType) {
        super(message);
        this.status = status;
        this.infoType = infoType;
    }

}