package com.ecommerce.inventory.exception;

public class InventoryNotFoundException extends RuntimeException{
    public InventoryNotFoundException(String skuCode) {
        super("Inventory not found with skuCode: " + skuCode);
    }
}
