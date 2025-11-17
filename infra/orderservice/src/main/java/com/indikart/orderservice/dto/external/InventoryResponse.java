package com.indikart.orderservice.dto.external;

import lombok.Data;

@Data
public class InventoryResponse {
    private Long id;
    private Long productId;
    private Integer availableQuantity;
}
