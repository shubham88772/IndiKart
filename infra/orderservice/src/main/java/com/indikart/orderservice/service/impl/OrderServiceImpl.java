package com.indikart.orderservice.service.impl;

import com.indikart.orderservice.client.InventoryClient;
import com.indikart.orderservice.client.ProductClient;
import com.indikart.orderservice.dto.external.InventoryResponse;
import com.indikart.orderservice.dto.external.ProductResponse;
import com.indikart.orderservice.dto.request.CreateOrderRequest;
import com.indikart.orderservice.dto.response.ApiResponse;
import com.indikart.orderservice.dto.response.OrderResponse;
import com.indikart.orderservice.entity.Order;
import com.indikart.orderservice.entity.OrderItem;
import com.indikart.orderservice.entity.OrderStatus;
import com.indikart.orderservice.exception.ResourceNotFoundException;
import com.indikart.orderservice.repository.OrderRepository;
import com.indikart.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    // Feign Clients
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        // Validate items + fetch product + inventory
        request.getItems().forEach(item -> {

            ApiResponse<ProductResponse> productApi = productClient.getProductById(item.getProductId());
            ProductResponse product = productApi.getData();

            if (product == null) {
                throw new ResourceNotFoundException("Product not found with id: " + item.getProductId());
            }

            ApiResponse<InventoryResponse> invApi =
                    inventoryClient.getInventoryByProductId(item.getProductId());

            InventoryResponse inventory = invApi.getData();

            if (inventory == null) {
                throw new ResourceNotFoundException("Inventory not found for productId: " + item.getProductId());
            }

            if (inventory.getAvailableQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for productId: " + item.getProductId()
                );
            }

            // overwrite price from product-service (important)
            item.setUnitPrice(product.getPrice());
        });

        // Calculate total amount
        BigDecimal totalAmount = request.getItems().stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = Order.builder()
                .userId(request.getUserId())
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Create items
        List<OrderItem> items = request.getItems().stream()
                .map(i -> OrderItem.builder()
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        order.setItems(items);

        // IMPORTANT => reduce stock BEFORE saving order
        request.getItems().forEach(item -> {
            inventoryClient.reduceStock(item.getProductId(), item.getQuantity());
        });

        Order saved = orderRepository.save(order);

        return modelMapper.map(saved, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .collect(Collectors.toList());
    }
}
