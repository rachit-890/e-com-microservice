package com.ecommerce.inventory.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {

    @NotBlank(message = "SKU Code is mandatory")
    private String skuCode;

    @Min(value = 0, message = "Quantity should be greater than 0")
    private Integer quantity;
}
