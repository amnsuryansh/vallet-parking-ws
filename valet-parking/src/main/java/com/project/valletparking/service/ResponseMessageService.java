package com.project.valletparking.service;

import com.project.valletparking.enums.InfoType;
import com.project.valletparking.model.ResponseMessage;

/**
 * Service interface for creating standardized response messages
 */
public interface ResponseMessageService {

    /**
     * Create a response with data, info type, and message
     */
    <T> ResponseMessage<T> createResponse(T data, InfoType infoType, String message);

    /**
     * Create a success response with data and message
     */
    <T> ResponseMessage<T> createSuccessResponse(T data, String message);

    /**
     * Create an error response with message
     */
    <T> ResponseMessage<T> createErrorResponse(String message);

    /**
     * Create a warning response with message
     */
    <T> ResponseMessage<T> createWarningResponse(String message);
}