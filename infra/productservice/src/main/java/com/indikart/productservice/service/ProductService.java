package com.indikart.productservice.service;

import com.indikart.productservice.dto.ProductRequest;
import com.indikart.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    public ProductResponse createProduct(ProductRequest productRequest);
    public List<ProductResponse> getAllProducts();
    public ProductResponse getProductById(Long id);
    public ProductResponse updateProduct(Long id,ProductRequest productRequest);
    public void deleteProduct(Long id);

}
