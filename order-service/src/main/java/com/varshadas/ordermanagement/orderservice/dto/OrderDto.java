package com.varshadas.ordermanagement.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private List<OrderItemDto> orderItems;


}




