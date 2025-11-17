package com.indikart.inventoryservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequest {
    @NotNull(message = "productId is required")
    private Long productId;
    @NotNull(message = "availableQuantity is required")
    @Min(value = 0, message = "availableQuantity must be >= 0")
    private int availableQuantity;
}
