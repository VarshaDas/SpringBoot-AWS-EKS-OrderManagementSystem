package com.varshadas.ordermanagement.orderservice.client;

import com.varshadas.ordermanagement.orderservice.dto.OrderItemDto;
import com.varshadas.ordermanagement.orderservice.dto.ProductAvailabilityResponse;
import com.varshadas.ordermanagement.orderservice.dto.ProductDto;
import com.varshadas.ordermanagement.orderservice.dto.ProductAvailability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceClient  {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceClient.class);

    private final RestTemplate restTemplate;

    @Value("${product.service.url}")
    private String productServiceUrl;

    public ProductServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ProductAvailability> checkProductAvailability(List<OrderItemDto> products) {
        String checkAvailabilityUrl = productServiceUrl + "/availability";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<OrderItemDto>> requestEntity = new HttpEntity<>(products, headers);

        try {
            ResponseEntity<ProductAvailabilityResponse> response = restTemplate.exchange(
                    checkAvailabilityUrl,
                    HttpMethod.POST,
                    requestEntity,
                    ProductAvailabilityResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody().getProductAvailabilityList();
            } else {
                logger.error("Failed to check product availability. Status code: {}", response.getStatusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            logger.error("Exception occurred while checking product availability: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
