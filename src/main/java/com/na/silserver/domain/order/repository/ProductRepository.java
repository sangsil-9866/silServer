package com.na.silserver.domain.order.repository;

import com.na.silserver.domain.order.entity.Product;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface ProductRepository extends JpaRepository<Product, String> {

}
