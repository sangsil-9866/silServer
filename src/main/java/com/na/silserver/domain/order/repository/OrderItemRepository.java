package com.na.silserver.domain.order.repository;

import com.na.silserver.domain.order.entity.OrderEntity;
import com.na.silserver.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
}
