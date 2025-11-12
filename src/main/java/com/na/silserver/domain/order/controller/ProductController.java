package com.na.silserver.domain.order.controller;

import com.na.silserver.domain.order.dto.ProductDto;
import com.na.silserver.domain.order.service.ProductService;
import com.na.silserver.global.response.MessageResponse;
import com.na.silserver.global.util.UtilMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
@Tag(name = "상품관리", description = "상품 API")
public class ProductController {

    private final ProductService productService;
    private final UtilMessage utilMessage;

    @Operation(summary = "상품목록", description = "상품목록")
    @GetMapping
    public ResponseEntity<List<ProductDto.Response>> productlist() {
        return ResponseEntity.ok(productService.productlist());
    }

    @Operation(summary = "상품상세", description = "상품상세")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto.Response> productDetail(@PathVariable String id) {
        return ResponseEntity.ok(productService.productDetail(id));
    }

    @Operation(summary = "상품등록", description = "상품등록")
    @PostMapping
    public ResponseEntity<ProductDto.Response> productCreate(@ParameterObject ProductDto.CreateRequest request) {
        return ResponseEntity.ok(productService.productCreate(request));
    }
    
    @Operation(summary = "상품수정", description = "상품수정")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> productModify(@PathVariable String id,
                                                     @ParameterObject ProductDto.ModifyRequest request) {
        productService.productModify(id, request);
        return ResponseEntity.ok(MessageResponse.of(utilMessage.getMessage("success.modify")));
    }
    
    @Operation(summary = "상품삭제", description = "상품삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> productDelete(@PathVariable String id) {
        productService.productDelete(id);
        return ResponseEntity.ok(MessageResponse.of(utilMessage.getMessage("success.delete")));
    }
}
