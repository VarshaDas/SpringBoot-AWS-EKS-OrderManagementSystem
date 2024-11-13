package com.varshadas.ordermanagement.orderservice.dto;

import com.varshadas.ordermanagement.orderservice.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;

    public OrderResponse(String status) {
        this.status = status;
    }

    private String status;

    public OrderResponse(Order order) {
        this.status = order.getOrderStatus();
        this.orderId = order.getId();
    }
}


