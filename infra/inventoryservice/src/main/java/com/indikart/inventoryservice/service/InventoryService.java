package com.indikart.inventoryservice.service;

import com.indikart.inventoryservice.dto.request.InventoryRequest;
import com.indikart.inventoryservice.dto.response.InventoryResponse;

import java.util.List;

public interface InventoryService {
    InventoryResponse create(InventoryRequest request);
    InventoryResponse getById(Long id);
    List<InventoryResponse> getAll();
    InventoryResponse update(Long id, InventoryRequest request);
    void delete(Long id);

}
