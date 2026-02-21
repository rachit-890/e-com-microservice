package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .categoryId(product.getCategory().getId())
                .quantity(product.getQuantity())
                .build();
    }
    public Product toEntity(ProductDto productDto, Category category){
        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .imageUrl(productDto.getImageUrl())
                .price(productDto.getPrice())
                .discountPrice(productDto.getDiscountPrice())
                .quantity(productDto.getQuantity())
                .brand(productDto.getBrand())
                .category(category)
                .build();
    }
}
