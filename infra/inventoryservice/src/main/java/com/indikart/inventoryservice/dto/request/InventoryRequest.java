package com.indikart.inventoryservice.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class InventoryRequest {
    @NotNull
    private Long productId;
    @NotNull
    private Integer availableQuantity;
}
