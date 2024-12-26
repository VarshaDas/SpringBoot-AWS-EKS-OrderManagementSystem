package com.varshadas.ordermanagement.productservice.service;

import com.varshadas.ordermanagement.productservice.dto.ProductAvailability;
import com.varshadas.ordermanagement.productservice.dto.ProductAvailabilityResponse;
import com.varshadas.ordermanagement.productservice.dto.ProductDTO;
import com.varshadas.ordermanagement.productservice.entity.Product;
import com.varshadas.ordermanagement.productservice.exception.ResourceNotFoundException;
import com.varshadas.ordermanagement.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ProductService {

    ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

//        public List<ProductDTO> getAllProducts() {
//            return productRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
//        }
//
//        public ProductDTO getProductById(Long id) {
//            return productRepository.findById(id).map(this::convertToDTO).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
//        }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);

    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setPrice(productDTO.getPrice());
        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }
//
//        public void deleteProduct(Long id) {
//            Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
//            productRepository.delete(existingProduct);
//        }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getSkuCode(), product.getDescription(), product.getQuantity(), product.getPrice());
    }

    private Product convertToEntity(ProductDTO productDTO) {
        return new Product(productDTO.getId(), productDTO.getName(), productDTO.getSkuCode(), productDTO.getDescription(), productDTO.getQuantity(), productDTO.getPrice());
    }

    public ProductAvailabilityResponse checkProductAvailability(List<ProductDTO> products) {
        ProductAvailabilityResponse productAvailabilityResponse = ProductAvailabilityResponse.builder().build();
        productAvailabilityResponse.setProductAvailabilityList(products.stream().map(productDTO -> {
            Product product = productRepository.findBySkuCode(productDTO.getSkuCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU code " + productDTO.getSkuCode()));
            boolean available = product.getQuantity() >= productDTO.getQuantity();
            return new ProductAvailability(product.getSkuCode(), available);
        }).collect(Collectors.toList()));

        return productAvailabilityResponse;
    }

    public void reduceProductQuantity(String skuCode, int quantity) {
        // Find the product by SKU code
        Optional<Product> optionalProduct = productRepository.findBySkuCode(skuCode);

        // Check if the product is present
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get(); // Get the product object

            // Check if the quantity is sufficient
            if (product.getQuantity() >= quantity) {
                product.setQuantity(product.getQuantity() - quantity); // Reduce the quantity
                productRepository.save(product); // Save the updated product
            }
            // well we are checking availability
//                else {
//                    // Handle case where there is insufficient stock
//                  log.error("Insufficient stock for SKU: " + skuCode);
//                }
        } else {
            // Handle case where product is not found
            log.error("Product not found for SKU: " + skuCode);
        }
    }


    public void increaseProductQuantity(String skuCode, int quantity) {

        Optional<Product> optionalProduct = productRepository.findBySkuCode(skuCode);

        // Check if the product is present
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get(); // Get the product object

            // Increase the product's quantity by the specified amount
            product.setQuantity(product.getQuantity() + quantity);

            // Save the updated product to the database
            productRepository.save(product);
            log.info("Increased stock for SKU: {} by {} units. New stock: {}", skuCode, quantity, product.getQuantity());
        } else {
            // Handle case where product is not found
            log.error("Product not found for SKU: " + skuCode);
        }
    }

}


