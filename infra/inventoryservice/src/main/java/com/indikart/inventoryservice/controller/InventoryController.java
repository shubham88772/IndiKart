package com.indikart.inventoryservice.controller;

import com.indikart.inventoryservice.dto.request.InventoryRequest;
import com.indikart.inventoryservice.dto.response.ApiResponse;
import com.indikart.inventoryservice.dto.response.InventoryResponse;
import com.indikart.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(summary = "Get inventory by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(inventoryService.getById(id)));
    }

    @Operation(summary = "Get inventory by product id")
    @GetMapping("/product/{productId}")
    @Cacheable(value = "inventory-by-product-id", key = "#productId")
    public ResponseEntity<ApiResponse<InventoryResponse>> getByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(new ApiResponse<>(inventoryService.getByProductId(productId)));
    }

    @Operation(summary = "Create inventory")
    @PostMapping
    @CacheEvict(value = "inventory-by-product-id", allEntries = true)
    public ResponseEntity<ApiResponse<InventoryResponse>> create(@RequestBody @Valid InventoryRequest request) {
        return ResponseEntity.status(201).body(new ApiResponse<>(inventoryService.create(request)));
    }

    @Operation(summary = "Update inventory")
    @PutMapping("/{id}")
    @CacheEvict(value = "inventory-by-product-id", allEntries = true)
    public ResponseEntity<ApiResponse<InventoryResponse>> update(@PathVariable Long id,
                                                                 @RequestBody @Valid InventoryRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(inventoryService.update(id, request)));
    }

    @Operation(summary = "Reduce stock synchronously")
    @PutMapping("/reduce/{productId}")
    @CacheEvict(value = "inventory-by-product-id", key = "#productId")
    public ResponseEntity<ApiResponse<InventoryResponse>> reduceStock(@PathVariable Long productId,
                                                                      @RequestParam Integer quantity) {
        return ResponseEntity.ok(new ApiResponse<>(inventoryService.reduceStock(productId, quantity)));
    }

    @Operation(summary = "Delete inventory")
    @DeleteMapping("/{id}")
    @CacheEvict(value = "inventory-by-product-id", allEntries = true)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inventoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all")
    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(inventoryService.getAll()));
    }
}