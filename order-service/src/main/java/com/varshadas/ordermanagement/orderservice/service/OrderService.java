package com.varshadas.ordermanagement.orderservice.service;

import com.varshadas.ordermanagement.orderservice.dto.OrderDto;
import com.varshadas.ordermanagement.orderservice.dto.OrderResponse;
import com.varshadas.ordermanagement.orderservice.dto.ProductAvailability;
import jakarta.transaction.Transactional;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    OrderResponse createOrder(OrderDto orderDto);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    void deleteOrder(Long id);

    OrderResponse cancelOrder(Long id);

    boolean isProductAvailable(String skuCode, List<ProductAvailability> availabilityList);
}
