package com.na.silserver.domain.order.controller;

import com.na.silserver.domain.order.dto.OrderDto;
import com.na.silserver.domain.order.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "주문관리", description = "주문 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto.Response> create(@RequestBody OrderDto.CreateRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto.Response>> list() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto.Response> get(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancel(@PathVariable String id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok("주문이 취소되었습니다.");
    }

    /**
     * ✅ 결제 완료 API
     */
    @PutMapping("/{id}/pay")
    public ResponseEntity<String> pay(@PathVariable String id) {
        orderService.completePayment(id);
        return ResponseEntity.ok("결제가 완료되었습니다.");
    }
}
