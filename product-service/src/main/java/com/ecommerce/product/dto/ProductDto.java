package com.ecommerce.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    private String imageUrl;
    @Positive(message = "Price cannot be negative")
    private Double price;
    private Double discountPrice;
    @Min(value = 0,message = "Quantity cannot be negative")
    private int quantity;
    private String brand;
    @NotBlank(message = "Category id cannot be blank")
    private Long categoryId;
}
