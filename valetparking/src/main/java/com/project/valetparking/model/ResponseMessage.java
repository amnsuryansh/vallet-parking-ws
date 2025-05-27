package com.project.valetparking.model;

import com.project.valetparking.enums.InfoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard response format for all APIs
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage<T> {
    private T data;
    private InfoType infoType;
    private String message;
}
