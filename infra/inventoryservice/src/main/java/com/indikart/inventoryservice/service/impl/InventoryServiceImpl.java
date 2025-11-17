package com.indikart.inventoryservice.service.impl;

import com.indikart.inventoryservice.dto.request.InventoryRequest;
import com.indikart.inventoryservice.dto.response.InventoryResponse;
import com.indikart.inventoryservice.entity.Inventory;
import com.indikart.inventoryservice.exception.ResourceNotFoundException;
import com.indikart.inventoryservice.repository.InventoryRepository;
import com.indikart.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public InventoryResponse create(InventoryRequest request) {

        Inventory inventory = Inventory.builder()
                .productId(request.getProductId())
                .availableQuantity(request.getAvailableQuantity())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Inventory saved = inventoryRepository.save(inventory);

        return modelMapper.map(saved, InventoryResponse.class);
    }

    @Override
    public InventoryResponse getById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));

        return modelMapper.map(inventory, InventoryResponse.class);
    }

    @Override
    public List<InventoryResponse> getAll() {
        return inventoryRepository.findAll()
                .stream()
                .map(inv -> modelMapper.map(inv, InventoryResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public InventoryResponse update(Long id, InventoryRequest request) {

        Inventory existing = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));

        existing.setProductId(request.getProductId());
        existing.setAvailableQuantity(request.getAvailableQuantity());
        existing.setUpdatedAt(LocalDateTime.now());

        Inventory updated = inventoryRepository.save(existing);

        return modelMapper.map(updated, InventoryResponse.class);
    }

    @Override
    public void delete(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventory not found with id: " + id);
        }
        inventoryRepository.deleteById(id);
    }
}
