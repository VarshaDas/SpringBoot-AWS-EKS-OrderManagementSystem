package com.varshadas.ordermanagement.orderservice.repository;


import com.varshadas.ordermanagement.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
}


