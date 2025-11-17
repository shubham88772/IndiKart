package com.indikart.inventoryservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class InventoryResponse {
    private Long id;
    private Long productId;
    private int availableQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
