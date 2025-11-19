
//
//@Service
//@RequiredArgsConstructor
//public class ProductServiceImpl implements ProductService {
//
//    private final ProductRepository productRepository;
//    private final ModelMapper modelMapper;
//
//    @Override
//    public ProductResponse createProduct(ProductRequest productRequest) {
//        Product mapped=Product.builder()
//                .name(productRequest.getName())
//                .price(productRequest.getPrice())
//                .description(productRequest.getDescription())
//                .quantity(productRequest.getQuantity())
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        Product saved=productRepository.save(mapped);
//        return modelMapper.map(saved, ProductResponse.class);
//    }
//
//    @Override
//    public List<ProductResponse> getAllProducts() {
//       List<Product> found= productRepository.findAll();
//       return found.stream().map(product -> modelMapper.map(product, ProductResponse.class))
//               .collect(Collectors.toList());
//
//    }
//
//    @Override
//    public ProductResponse getProductById(Long id) {
//       Product found= productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id:"+id));
//        return modelMapper.map(found, ProductResponse.class);
//    }
//
//    @Override
//    public ProductResponse updateProduct(Long id,ProductRequest productRequest) {
//        Product existing=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id:"+id));
//        existing.setName(productRequest.getName());
//        existing.setDescription(productRequest.getDescription());
//        existing.setPrice(productRequest.getPrice());
//        existing.setQuantity(productRequest.getQuantity());
//        existing.setUpdatedAt(LocalDateTime.now());
//
//        Product updated = productRepository.save(existing);
//
//        return modelMapper.map(updated, ProductResponse.class);
//    }
//
//    @Override
//    public void deleteProduct(Long id) {
//        Product found=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id:->"+id));
//        productRepository.deleteById(id);
//    }
//    @Override
//    public Page<ProductResponse> searchProducts(String name, String description,
//                                                BigDecimal minPrice, BigDecimal maxPrice,
//                                                Integer minQty,
//                                                int page, int size, String sortBy, String direction) {
//
//        Sort sort = "desc".equalsIgnoreCase(direction) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
//        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), sort);
//
//        Specification<Product> spec = Specification.where(ProductSpecification.nameContains(name))
//                .and(ProductSpecification.descriptionContains(description))
//                .and(ProductSpecification.priceGte(minPrice))
//                .and(ProductSpecification.priceLte(maxPrice))
//                .and(ProductSpecification.quantityGte(minQty));
//
//        Page<Product> productPage = productRepository.findAll(spec, pageable);
//
//
//
//        return productPage.map(product -> modelMapper.map(product, ProductResponse.class));
//    }
//    // ===========================
//    // PagedResponse Converter
//    // ===========================
//    private PagedResponse<ProductResponse> convertToPagedResponse(Page<ProductResponse> page) {
//        return new PagedResponse<>(
//                page.getContent(),
//                page.getNumber(),
//                page.getSize(),
//                page.getTotalElements(),
//                page.getTotalPages(),
//                page.isLast()
//        );
//    }
//}
package com.indikart.productservice.service.impl;

import com.indikart.productservice.dto.request.ProductRequest;
import com.indikart.productservice.dto.response.PagedResponse;
import com.indikart.productservice.dto.response.ProductResponse;
import com.indikart.productservice.entity.Product;
import com.indikart.productservice.exception.ResourceNotFoundException;
import com.indikart.productservice.repository.ProductRepository;
import com.indikart.productservice.service.ProductService;
import com.indikart.productservice.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    // ===========================
    // CREATE
    // ===========================
    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product mapped = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .quantity(productRequest.getQuantity())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Product saved = productRepository.save(mapped);
        return modelMapper.map(saved, ProductResponse.class);
    }

    // ===========================
    // GET ALL
    // ===========================
    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

    // ===========================
    // GET BY ID
    // ===========================
    @Cacheable(value = "product-by-id-v2", key = "#id")
    @Override
    public ProductResponse getProductById(Long id) {
        Product found = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return modelMapper.map(found, ProductResponse.class);
    }


    // ===========================
    // UPDATE
    // ===========================
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        existing.setName(productRequest.getName());
        existing.setDescription(productRequest.getDescription());
        existing.setPrice(productRequest.getPrice());
        existing.setQuantity(productRequest.getQuantity());
        existing.setUpdatedAt(LocalDateTime.now());

        Product updated = productRepository.save(existing);
        return modelMapper.map(updated, ProductResponse.class);
    }

    // ===========================
    // DELETE
    // ===========================
    @Override
    public void deleteProduct(Long id) {
        Product found = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id -> " + id));
        productRepository.delete(found);
    }

    // ===========================
    // SEARCH WITH PAGINATION & FILTERS
    // ===========================
    @Override
    public PagedResponse<ProductResponse> searchProducts(String name,
                                                         String description,
                                                         BigDecimal minPrice,
                                                         BigDecimal maxPrice,
                                                         Integer minQty,
                                                         int page,
                                                         int size,
                                                         String sortBy,
                                                         String direction) {

        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), sort);

        Specification<Product> spec = Specification.where(ProductSpecification.nameContains(name))
                .and(ProductSpecification.descriptionContains(description))
                .and(ProductSpecification.priceGte(minPrice))
                .and(ProductSpecification.priceLte(maxPrice))
                .and(ProductSpecification.quantityGte(minQty));

        Page<Product> pageResult = productRepository.findAll(spec, pageable);

        Page<ProductResponse> mappedPage = pageResult.map(
                product -> modelMapper.map(product, ProductResponse.class)
        );

        return convertToPagedResponse(mappedPage);
    }

    // ===========================
    // PagedResponse Converter
    // ===========================
    private PagedResponse<ProductResponse> convertToPagedResponse(Page<ProductResponse> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
