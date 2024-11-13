package com.varshadas.ordermanagement.orderservice.kafka;

import com.varshadas.ordermanagement.orderservice.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderProducer {

    private static final String ORDER_PLACED_TOPIC = "order_placed";
    private static final String ORDER_CANCELLED_TOPIC = "order_cancelled";

    @Autowired
    private KafkaTemplate<String, OrderDto> kafkaTemplate;

    /**
     * Sends an event to the appropriate Kafka topic based on the event type.
     *
     * @param orderDto The details of the order.
     * @param eventType Type of event, either "placed" or "cancelled".
     */
    public void sendOrderEvent(OrderDto orderDto, String eventType) {
        String topic = eventType.equalsIgnoreCase("cancelled") ? ORDER_CANCELLED_TOPIC : ORDER_PLACED_TOPIC;
        Long orderId = orderDto.getId();

        try {
            log.info("Sending '{}' event for order ID: {}", eventType, orderId);
            kafkaTemplate.send(topic, orderDto);
            log.info("'{}' event sent successfully for order ID: {}", eventType, orderId);
        } catch (Exception e) {
            log.error("Failed to send '{}' event for order ID: {}. Error: {}", eventType, orderId, e.getMessage());
        }
    }
}
