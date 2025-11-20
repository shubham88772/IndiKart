package com.indikart.orderservice.client;

import com.indikart.orderservice.dto.external.ProductResponse;
import com.indikart.orderservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${clients.product.url:http://localhost:8081/api/v1/products}")
public interface ProductClient {
    @GetMapping("/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable("id") Long id);
}
