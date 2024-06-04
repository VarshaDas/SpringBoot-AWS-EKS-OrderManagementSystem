package com.varshadas.ordermanagement.orderservice.controller;

import com.varshadas.ordermanagement.orderservice.client.ProductServiceClient;
import com.varshadas.ordermanagement.orderservice.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductServiceClient productServiceClient;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderDto orderDto) {
        List<ProductDto> availabilityRequests = orderDto.getOrderItems().stream()
                .map(item -> ProductDto.builder()
                        .skuCode(item.getSkuCode())
                        .price(item.getPrice())
                        .quantity(item.getQuantity()).build())
                .collect(Collectors.toList());

        List<ProductAvailability> availabilityList = productServiceClient.checkProductAvailability(availabilityRequests);


        log.info("availabilityList {}", availabilityList);
        for (ProductAvailability productAvailability : availabilityList) {
            log.info("productAvailability {}", productAvailability);
            if (!productAvailability.isAvailable()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new OrderResponse(null, "Product " + productAvailability.getSkuCode() + " is not available"));
            }
        }

        OrderResponse orderResponse = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }


    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}

