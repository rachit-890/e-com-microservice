package com.ecommerce.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotBlank(message = "SKU code is required")
    private String skuCode;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

//    @NotBlank(message = "Order status is required")
//    @Pattern(regexp = "PLACED|SHIPPED|DELIVERED|CANCELLED",
//            message = "Status must be PLACED, SHIPPED, DELIVERED, or CANCELLED")
    private String orderStatus;
}