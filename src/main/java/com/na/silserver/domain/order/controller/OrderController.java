package com.na.silserver.domain.order.controller;

import com.na.silserver.domain.order.dto.OrderDto;
import com.na.silserver.domain.order.service.OrderService;
import com.na.silserver.global.response.ApiResponse;
import com.na.silserver.global.util.UtilMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "주문관리", description = "주문 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final UtilMessage utilMessage;

    @Operation(summary = "주문목록", description = "주문목록")
    @GetMapping
    public ResponseEntity<List<OrderDto.Response>> orderList() {
        return ResponseEntity.ok(orderService.orderList());
    }

    @Operation(summary = "주문상세", description = "주문상세")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto.Response> orderDetail(@PathVariable String id) {
        return ResponseEntity.ok(orderService.orderDetail(id));
    }

    @Operation(summary = "주문등록", description = "주문등록")
    @PostMapping
    public ResponseEntity<OrderDto.Response> orderCreate(@ParameterObject OrderDto.CreateRequest request) {
        return ResponseEntity.ok(orderService.orderCreate(request));
    }

    @Operation(summary = "주문취소", description = "주문취소")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> orderCancel(@PathVariable String id) {
        orderService.orderCancel(id);
        return ResponseEntity.ok(ApiResponse.of(utilMessage.getMessage("order.cancel")));
    }

    /**
     * ✅ 결제 완료 API
     */
    @Operation(summary = "결제완료", description = "결제완료")
    @PutMapping("/{id}/pay")
    public ResponseEntity<ApiResponse> pay(@PathVariable String id) {
        orderService.pay(id);
        return ResponseEntity.ok(ApiResponse.of(utilMessage.getMessage("order.complete")));
    }
}
