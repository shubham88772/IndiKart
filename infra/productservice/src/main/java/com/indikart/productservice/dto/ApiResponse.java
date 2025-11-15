package com.indikart.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ErrorResponse error;
    private Instant timestamp = Instant.now();

    public ApiResponse() {}

    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
    }

    public ApiResponse(ErrorResponse error) {
        this.success = false;
        this.error = error;
    }


}
