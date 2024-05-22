package com.varshadas.ordermanagement.productservice.controller;

import com.varshadas.ordermanagement.productservice.dto.ProductDTO;
import com.varshadas.ordermanagement.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


    @RestController
    @RequestMapping("/api/products")
    public class ProductController {
        @Autowired
        private ProductService productService;

//        @GetMapping
//        public List<ProductDTO> getAllProducts() {
//            return productService.getAllProducts();
//        }

//        @GetMapping("/{id}")
//        public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
//            ProductDTO productDTO = productService.getProductById(id);
//            return ResponseEntity.ok(productDTO);
//        }

        @PostMapping
        public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
            ProductDTO createdProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(createdProduct);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        }
//
//        @DeleteMapping("/{id}")
//        public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
//            productService.deleteProduct(id);
//            return ResponseEntity.noContent().build();
//        }
    }

