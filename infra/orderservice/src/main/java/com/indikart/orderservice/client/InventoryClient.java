package com.indikart.orderservice.client;

import com.indikart.orderservice.dto.external.InventoryResponse;
import com.indikart.orderservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/api/v1/inventory/product/{productId}")
    ApiResponse<InventoryResponse> getInventoryByProductId(@PathVariable Long productId);

    @PutMapping("/api/v1/inventory/reduce/{productId}")
    ApiResponse<InventoryResponse> reduceStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    );
}
