package com.project.valletparking.service.impl;

import com.project.valletparking.enums.InfoType;
import com.project.valletparking.model.ResponseMessage;
import com.project.valletparking.service.ResponseMessageService;
import org.springframework.stereotype.Service;

/**
 * Implementation of ResponseMessageService
 */
@Service
public class ResponseMessageServiceImpl implements ResponseMessageService {

    /**
     * Create a response with data, info type, and message
     */
    @Override
    public <T> ResponseMessage<T> createResponse(T data, InfoType infoType, String message) {
        return new ResponseMessage<>(data, infoType, message);
    }

    /**
     * Create a success response with data and message
     */
    @Override
    public <T> ResponseMessage<T> createSuccessResponse(T data, String message) {
        return createResponse(data, InfoType.SUCCESS, message);
    }

    /**
     * Create an error response with message
     */
    @Override
    public <T> ResponseMessage<T> createErrorResponse(String message) {
        return createResponse(null, InfoType.ERROR, message);
    }

    /**
     * Create a warning response with message
     */
    @Override
    public <T> ResponseMessage<T> createWarningResponse(String message) {
        return createResponse(null, InfoType.WARNING, message);
    }
}
