package com.varshadas.ordermanagement.orderservice.service;

import com.varshadas.ordermanagement.orderservice.dto.OrderDto;
import com.varshadas.ordermanagement.orderservice.dto.OrderItemDto;
import com.varshadas.ordermanagement.orderservice.dto.OrderResponse;
import com.varshadas.ordermanagement.orderservice.dto.OrderService;
import com.varshadas.ordermanagement.orderservice.entity.Order;
import com.varshadas.ordermanagement.orderservice.entity.OrderItem;
import com.varshadas.ordermanagement.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDto(order);
    }

//    @Override
//    @Transactional
//    public OrderResponse createOrder(OrderDto orderDto) {
//        Order order = convertToEntity(orderDto);
//        order = orderRepository.save(order);
//        OrderResponse orderResponse = OrderResponse.builder()
//                .orderId(order.getId())
//                //todo - change later
//                .status("Confirmed")
//                .build();
//
//        return orderResponse;
//    }


    @Override
    @Transactional
    public OrderResponse createOrder(OrderDto orderDto) {
        // Convert OrderDto to Order entity
        Order order = convertToEntity(orderDto);

        // Iterate over each OrderItemDto in the OrderDto
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
            // Create an OrderItem entity and set its properties
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(orderItemDto.getSkuCode());
            orderItem.setProductName(orderItemDto.getProductName());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setPrice(orderItemDto.getPrice());

            // Set the Order reference in the OrderItem entity
            orderItem.setOrder(order);
            orderItemList.add(orderItem);

            // Add the OrderItem to the Order's list of OrderItems
        }

        order.setOrderItems(orderItemList);


        // Save the Order entity to the database
        order = orderRepository.save(order);

        // Create and return the OrderResponse
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                //todo - change later
                .status("Confirmed")
                .build();

        return orderResponse;
    }


    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setOrderDate(orderDto.getOrderDate());

        // Update order items
        order.getOrderItems().clear();
        order.getOrderItems().addAll(orderDto.getOrderItems().stream().map(this::convertToEntity).collect(Collectors.toList()));

        order = orderRepository.save(order);
        return convertToDto(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDto convertToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .orderItems(order.getOrderItems().stream().map(this::convertToDto).collect(Collectors.toList()))
                .build();
    }

    private OrderItemDto convertToDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .skuCode(orderItem.getProductId())
                .productName(orderItem.getProductName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }

    private Order convertToEntity(OrderDto orderDto) {
        return Order.builder()
                .totalPrice(orderDto.getTotalPrice())
                .orderDate(orderDto.getOrderDate())
                .orderItems(orderDto.getOrderItems().stream().map(this::convertToEntity).collect(Collectors.toList()))
                .build();
    }

    private OrderItem convertToEntity(OrderItemDto orderItemDto) {
        return OrderItem.builder()
                .productId(orderItemDto.getSkuCode())
                .productName(orderItemDto.getProductName())
                .quantity(orderItemDto.getQuantity())
                .price(orderItemDto.getPrice())
                .build();
    }
}
