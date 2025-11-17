package com.indikart.inventoryservice.controller;

import com.indikart.inventoryservice.dto.response.ApiResponse;
import com.indikart.inventoryservice.dto.request.InventoryRequest;
import com.indikart.inventoryservice.dto.response.InventoryResponse;
import com.indikart.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
@Tag(name = "Inventory Service", description = "Manage inventory stock levels for products")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @Operation(summary = "Create inventory record")
    public ResponseEntity<ApiResponse<InventoryResponse>> create(@Valid @RequestBody InventoryRequest request) {
        InventoryResponse created = inventoryService.create(request);
        return ResponseEntity.ok(new ApiResponse<>(created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inventory record by ID")
    public ResponseEntity<ApiResponse<InventoryResponse>> getById(@PathVariable Long id) {
        InventoryResponse response = inventoryService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping
    @Operation(summary = "Get all inventory records")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getAll() {
        List<InventoryResponse> list = inventoryService.getAll();
        return ResponseEntity.ok(new ApiResponse<>(list));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update inventory record")
    public ResponseEntity<ApiResponse<InventoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody InventoryRequest request
    ) {
        InventoryResponse updated = inventoryService.update(id, request);
        return ResponseEntity.ok(new ApiResponse<>(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete inventory record")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        inventoryService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Inventory deleted successfully"));
    }
}
