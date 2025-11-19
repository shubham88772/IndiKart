package com.indikart.productservice.specification;

import com.indikart.productservice.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Product> descriptionContains(String desc) {
        return (root, query, cb) -> {
            if (desc == null || desc.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("description")), "%" + desc.toLowerCase() + "%");
        };
    }

    public static Specification<Product> priceGte(BigDecimal minPrice) {
        return (root, query, cb) -> {
            if (minPrice == null) return cb.conjunction();
            return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
        };
    }

    public static Specification<Product> priceLte(BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (maxPrice == null) return cb.conjunction();
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    public static Specification<Product> quantityGte(Integer minQty) {
        return (root, query, cb) -> {
            if (minQty == null) return cb.conjunction();
            return cb.greaterThanOrEqualTo(root.get("quantity"), minQty);
        };
    }
}
