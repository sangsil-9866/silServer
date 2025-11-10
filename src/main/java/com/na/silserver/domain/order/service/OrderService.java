package com.na.silserver.domain.order.service;

import com.na.silserver.domain.order.dto.OrderDto;
import com.na.silserver.domain.order.dto.OrderItemDto;
import com.na.silserver.domain.order.entity.OrderEntity;
import com.na.silserver.domain.order.entity.OrderItem;
import com.na.silserver.domain.order.entity.Product;
import com.na.silserver.domain.order.enums.OrderStatus;
import com.na.silserver.domain.order.repository.OrderRepository;
import com.na.silserver.domain.order.repository.ProductRepository;
import com.na.silserver.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UtilMessage utilMessage;

    /**
     * 주문 전체 조회
     */
    public List<OrderDto.Response> orderList() {
        return orderRepository.findAll().stream()
                .map(OrderDto.Response::from)
                .toList();
    }

    /**
     * 주문 단건 조회
     */
    public OrderDto.Response orderDetail(String orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data")));
        return OrderDto.Response.from(order);
    }


    /**
     * 주문 생성 (재고 차감 포함)
     *
     */
    public OrderDto.Response orderCreate(OrderDto.CreateRequest request) {
        OrderEntity order = new OrderEntity();

        for (OrderItemDto.CreateRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data")));

            // ✅ 재고 차감
            product.decreaseStock(itemReq.getQuantity());

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .build();

            order.getOrderItems().add(item);
        }

        OrderEntity saved = orderRepository.save(order);
        return OrderDto.Response.from(saved);
    }

    /**
     * 주문 취소 (재고 복구)
     */
    public void orderCancel(String orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data")));

        // ✅ 주문 상태 변경
        order.cancel();

        // ✅ 상품 재고 복구
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.increaseStock(item.getQuantity());
        }
    }

    /**
     * ✅ 결제 완료 처리
     */
    public void pay(String orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data")));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException(utilMessage.getMessage("status.fail", new String[]{"결제"}));
        }
        order.setStatus(OrderStatus.PAID);
    }
}
