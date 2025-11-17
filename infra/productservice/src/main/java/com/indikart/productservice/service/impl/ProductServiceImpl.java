package com.indikart.productservice.service.impl;

import com.indikart.productservice.dto.request.ProductRequest;
import com.indikart.productservice.dto.response.ProductResponse;
import com.indikart.productservice.entity.Product;
import com.indikart.productservice.exception.ResourceNotFoundException;
import com.indikart.productservice.repository.ProductRepository;
import com.indikart.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product mapped=Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .quantity(productRequest.getQuantity())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Product saved=productRepository.save(mapped);
        return modelMapper.map(saved, ProductResponse.class);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
       List<Product> found= productRepository.findAll();
       return found.stream().map(product -> modelMapper.map(product, ProductResponse.class))
               .collect(Collectors.toList());

    }

    @Override
    public ProductResponse getProductById(Long id) {
       Product found= productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id:"+id));
        return modelMapper.map(found, ProductResponse.class);
    }

    @Override
    public ProductResponse updateProduct(Long id,ProductRequest productRequest) {
        Product existing=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id:"+id));
        existing.setName(productRequest.getName());
        existing.setDescription(productRequest.getDescription());
        existing.setPrice(productRequest.getPrice());
        existing.setQuantity(productRequest.getQuantity());
        existing.setUpdatedAt(LocalDateTime.now());

        Product updated = productRepository.save(existing);

        return modelMapper.map(updated, ProductResponse.class);
    }

    @Override
    public void deleteProduct(Long id) {
        Product found=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id:->"+id));
        productRepository.deleteById(id);
    }
}
