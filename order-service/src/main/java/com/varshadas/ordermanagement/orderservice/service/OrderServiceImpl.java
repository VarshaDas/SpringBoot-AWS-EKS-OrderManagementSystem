package com.varshadas.ordermanagement.orderservice.service;

import com.varshadas.ordermanagement.orderservice.dto.OrderDto;
import com.varshadas.ordermanagement.orderservice.dto.OrderItemDto;
import com.varshadas.ordermanagement.orderservice.dto.OrderResponse;
import com.varshadas.ordermanagement.orderservice.dto.ProductAvailability;
import com.varshadas.ordermanagement.orderservice.entity.Order;
import com.varshadas.ordermanagement.orderservice.entity.OrderItem;
import com.varshadas.ordermanagement.orderservice.entity.OrderStatus;
import com.varshadas.ordermanagement.orderservice.kafka.OrderProducer;
import com.varshadas.ordermanagement.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    OrderProducer orderProducer;

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDto(order);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderDto orderDto) {
        Order order = convertToEntity(orderDto);

        Order finalOrder = order;
        List<OrderItem> orderItemList = orderDto.getOrderItems().stream()
                .map(itemDto -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(itemDto.getSkuCode());
                    orderItem.setProductName(itemDto.getProductName());
                    orderItem.setQuantity(itemDto.getQuantity());
                    orderItem.setPrice(itemDto.getPrice());
                    orderItem.setOrder(finalOrder);
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItemList);
        order.setOrderStatus(OrderStatus.ORDER_PLACED.name());
        order = orderRepository.save(order);

        return OrderResponse.builder()
                .orderId(order.getId())
                .status("ORDER PLACED")
                .build();
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setOrderDate(orderDto.getOrderDate());

        order.getOrderItems().clear();
        order.getOrderItems().addAll(orderDto.getOrderItems().stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList()));

        order = orderRepository.save(order);
        return convertToDto(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    @Override
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if(!order.getOrderStatus().equals("ORDER_CANCELLED")){
            order.setOrderStatus("Order Cancelled");
            orderRepository.save(order);
        }


        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setOrderItems(order.getOrderItems().stream()
                .map(item -> new OrderItemDto(item.getProductId(), item.getQuantity()))  // Assuming constructor or converter for OrderItemDto
                .collect(Collectors.toList()));

        orderProducer.sendOrderEvent(orderDto, "cancelled");  // Send cancellation event with full order data
        return new OrderResponse(order);  // Return updated order response
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


    /**
     * Checks if the product is available based on the SKU code and availability list.
     *
     * @param skuCode          The SKU code of the product.
     * @param availabilityList The list of product availability.
     * @return true if the product is available; otherwise false.
     */
    @Override
    public boolean isProductAvailable(String skuCode, List<ProductAvailability> availabilityList) {
        return availabilityList.stream()
                .anyMatch(availability -> availability.getSkuCode().equals(skuCode) && availability.isAvailable());
    }
}
