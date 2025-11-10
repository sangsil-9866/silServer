package com.na.silserver.domain.order.service;

import com.na.silserver.domain.order.dto.ProductDto;
import com.na.silserver.domain.order.entity.Product;
import com.na.silserver.domain.order.repository.ProductRepository;
import com.na.silserver.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UtilMessage utilMessage;

    public List<ProductDto.Response> productlist() {
        return productRepository.findAll().stream()
                .map(ProductDto.Response::toDto)
                .toList();
    }

    public ProductDto.Response productDetail(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data")));
        return ProductDto.Response.toDto(product);
    }

    public ProductDto.Response productCreate(ProductDto.CreateRequest request) {
        return ProductDto.Response.toDto(productRepository.save(request.toEntity()));
    }

    public void productModify(String id, ProductDto.ModifyRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data")));
        product.modify(request);
    }

    public void productDelete(String id) {
        productRepository.deleteById(id);
    }
}
