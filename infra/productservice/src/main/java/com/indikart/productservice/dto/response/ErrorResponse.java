package com.indikart.productservice.dto.response;

import lombok.Data;

import java.util.List;
@Data
public class ErrorResponse {
    private String code;
    private String message;
    private List<String> details;

    public ErrorResponse() {}

    public ErrorResponse(String code, String message, List<String> details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

}
