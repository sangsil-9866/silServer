package com.na.silserver.domain.order.service;

import com.na.silserver.domain.order.dto.ProductDto;
import com.na.silserver.domain.order.entity.Product;
import com.na.silserver.domain.order.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDto create(ProductDto dto) {
        Product product = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .description(dto.getDescription())
                .build();
        return ProductDto.from(productRepository.save(product));
    }

    public List<ProductDto> getAll() {
        return productRepository.findAll().stream()
                .map(ProductDto::from)
                .toList();
    }

    public ProductDto get(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        return ProductDto.from(product);
    }

    public void delete(String id) {
        productRepository.deleteById(id);
    }
}
