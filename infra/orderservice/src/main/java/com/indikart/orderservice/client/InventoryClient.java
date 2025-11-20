package com.indikart.orderservice.client;

import com.indikart.orderservice.dto.external.InventoryResponse;
import com.indikart.orderservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "inventory-service",
        url = "${clients.inventory.url:http://localhost:8082/api/v1/inventory}"
)
public interface InventoryClient {

    @GetMapping("/product/{productId}")
    ApiResponse<InventoryResponse> getInventoryByProductId(@PathVariable("productId") Long productId);

    @PutMapping("/reduce/{productId}")
    ApiResponse<InventoryResponse> reduceStock(
            @PathVariable("productId") Long productId,
            @RequestParam("quantity") Integer quantity
    );
}
