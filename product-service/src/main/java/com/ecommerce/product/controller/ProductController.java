package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto){
        return ResponseEntity.ok(productService.createProduct(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,@RequestBody ProductDto dto){
        return ResponseEntity.ok(productService.updateProduct(id,dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }
    @GetMapping("/getAll")
    public ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                           @RequestParam(defaultValue = "asc") String direction){
        return ResponseEntity.ok(productService.getAllProducts(page,size,sortBy,direction));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchProducts(@RequestParam String keyword,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(productService.searchProducts(keyword,page,size));
    }
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductDto>> filterProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                productService.filterProducts(categoryId, minPrice, maxPrice, page, size)
        );
    }
    @GetMapping("/advanceFilter")
    public ResponseEntity<Page<ProductDto>> advanceFilter(@RequestParam(required = false) String keyword,
                                                          @RequestParam(required = false) Long categoryId,
                                                          @RequestParam(required = false) Double minPrice,
                                                          @RequestParam(required = false) Double maxPrice,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                          @RequestParam(defaultValue = "asc") String direction){
        return ResponseEntity.ok(productService.advanceFilter(keyword,categoryId,minPrice,maxPrice,page,size,sortBy,direction));
    }
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<ProductDto> uploadImage(@PathVariable String id,@RequestParam MultipartFile file) throws IOException{
        return ResponseEntity.ok(productService.updateImage(Long.parseLong(id),file));
    }
}
