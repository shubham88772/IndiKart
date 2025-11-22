package com.indikart.orderservice.client;

import com.indikart.orderservice.dto.external.ProductResponse;
import com.indikart.orderservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/v1/products/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable Long id);
}
