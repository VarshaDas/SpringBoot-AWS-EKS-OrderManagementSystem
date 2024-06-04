package com.varshadas.ordermanagement.orderservice.repository;

import com.varshadas.ordermanagement.orderservice.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}


