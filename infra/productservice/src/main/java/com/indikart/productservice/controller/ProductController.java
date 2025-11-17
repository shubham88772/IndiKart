package com.indikart.productservice.controller;

import com.indikart.productservice.dto.response.ApiResponse;
import com.indikart.productservice.dto.request.ProductRequest;
import com.indikart.productservice.dto.response.ProductResponse;
import com.indikart.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Operations related to product catalog")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a product")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(@RequestBody @Valid ProductRequest request) {
        ProductResponse created = productService.createProduct(request);
        return ResponseEntity.status(201).body(new ApiResponse<>(created));
    }

    @Operation(summary = "Get all products")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAll() {
        List<ProductResponse> list = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse<>(list));
    }

    @Operation(summary = "Get product by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable Long id) {
        ProductResponse resp = productService.getProductById(id);
        return ResponseEntity.ok(new ApiResponse<>(resp));
    }

    @Operation(summary = "Update a product")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(@PathVariable Long id,
                                                               @RequestBody @Valid ProductRequest request) {
        ProductResponse resp = productService.updateProduct(id, request);
        return ResponseEntity.ok(new ApiResponse<>(resp));
    }

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
