package com.indikart.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ErrorResponse error;
    private String timestamp = Instant.now().toString();

    public ApiResponse() {}

    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
        this.timestamp = Instant.now().toString();
    }

    public ApiResponse(ErrorResponse error) {
        this.success = false;
        this.error = error;
        this.timestamp = Instant.now().toString();
    }
}
