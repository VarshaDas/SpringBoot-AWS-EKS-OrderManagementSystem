package com.varshadas.ordermanagement.orderservice.controller;

import com.varshadas.ordermanagement.orderservice.client.ProductServiceClient;
import com.varshadas.ordermanagement.orderservice.dto.OrderDto;
import com.varshadas.ordermanagement.orderservice.dto.OrderItemDto;
import com.varshadas.ordermanagement.orderservice.dto.OrderResponse;
import com.varshadas.ordermanagement.orderservice.dto.ProductAvailability;
import com.varshadas.ordermanagement.orderservice.kafka.OrderProducer;
import com.varshadas.ordermanagement.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final ProductServiceClient productServiceClient;
    private final OrderProducer orderProducer;

    public OrderController(OrderService orderService, ProductServiceClient productServiceClient, OrderProducer orderProducer) {
        this.orderService = orderService;
        this.productServiceClient = productServiceClient;
        this.orderProducer = orderProducer;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderDto orderDto) {
        try {
            // Filter available items by checking with ProductService
            List<ProductAvailability> availabilityList = productServiceClient.checkProductAvailability(orderDto.getOrderItems());

            // Filter available items based on the availability response
            List<OrderItemDto> availableItems = orderDto.getOrderItems().stream()
                    .filter(item -> orderService.isProductAvailable(item.getSkuCode(), availabilityList))
                    .collect(Collectors.toList());

            // Handle case where no items are available
            if (availableItems.isEmpty()) {
                log.warn("No items available in the order request.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new OrderResponse("No items available for the order."));
            }
            // Update the order with available items only
            orderDto.setOrderItems(availableItems);

            // Save the order in the database
            OrderResponse orderResponse = orderService.createOrder(orderDto);

            if (orderResponse != null) {
                log.info("Order placed successfully, sending 'Order Placed' event to Kafka...");
                orderDto.setId(orderResponse.getOrderId());
                orderProducer.sendOrderEvent(orderDto, "placed");  // Send event to Kafka
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
        } catch (Exception e) {
            log.error("Error while creating order", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create order");
        }
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        try {

            OrderResponse orderResponse = orderService.cancelOrder(id);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            log.error("Error while cancelling order", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to cancel order");
        }
    }



}
