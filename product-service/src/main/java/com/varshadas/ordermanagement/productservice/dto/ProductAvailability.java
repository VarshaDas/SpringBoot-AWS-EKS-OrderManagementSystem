package com.varshadas.ordermanagement.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAvailability {
    private String skuCode;
    private boolean isAvailable;
}


