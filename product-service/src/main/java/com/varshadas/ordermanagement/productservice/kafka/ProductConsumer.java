package com.varshadas.ordermanagement.productservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.varshadas.ordermanagement.productservice.dto.OrderItemDto;
import com.varshadas.ordermanagement.productservice.dto.OrderMessage;
import com.varshadas.ordermanagement.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductConsumer {

    @Autowired
    ProductService productService;

    private static final String ORDER_PLACED_TOPIC = "order_placed";

    private static final String ORDER_CANCELLED_TOPIC = "order_cancelled";


    @KafkaListener(topics = ORDER_PLACED_TOPIC)
    public void consumeOrderPlaced(String orderMessage)  {

        log.info("Order received {}", orderMessage);

        ObjectMapper objectMapper = new ObjectMapper();
        OrderMessage orderDto = null;
        try {
             orderDto = objectMapper.readValue(orderMessage, OrderMessage.class);
            // Process the orderDto as needed
            log.info("Deserialized Order: {}", orderDto);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message", e);
            throw new RuntimeException("Failed to deserialize message" +e);
        }

        List<OrderItemDto> orderItems = orderDto.getOrderItems();

        if (orderItems != null) {
            // Now you can access only skuCode and quantity
            for (OrderItemDto item : orderItems) {
                String skuCode = item.getSkuCode();
                int quantity = item.getQuantity();

                productService.reduceProductQuantity(skuCode, quantity);
            }
        }
    }

    // Listener for order cancellation, increasing SKU levels
    @KafkaListener(topics = ORDER_CANCELLED_TOPIC)
    public void consumeOrderCancelled(String orderMessage) {
        log.info("Order received for cancellation: {}", orderMessage);

        ObjectMapper objectMapper = new ObjectMapper();
        OrderMessage orderDto = null;

        try {
             orderDto = objectMapper.readValue(orderMessage, OrderMessage.class);
            log.info("Deserialized Cancelled Order: {}", orderDto);

            List<OrderItemDto> orderItems = orderDto.getOrderItems();
            if (orderItems != null) {
                for (OrderItemDto item : orderItems) {
                    String skuCode = item.getSkuCode();
                    int quantity = item.getQuantity();
                    productService.increaseProductQuantity(skuCode, quantity);
                }
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize order cancellation message", e);
            throw new RuntimeException("Failed to deserialize order cancellation message", e);
        }
    }

}

