package com.ecommerce.inventory.mapper;

import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper{

    public Inventory toEntity(InventoryRequest inventoryRequest){
        Inventory inventory = new Inventory();
        inventory.setSkuCode(inventoryRequest.getSkuCode());
        inventory.setQuantity(inventoryRequest.getQuantity());
        return inventory;
    }

    public InventoryResponse toResponse(Inventory inventory){
        return new InventoryResponse(inventory.getSkuCode(),
                inventory.getQuantity()>0,
                inventory.getQuantity());
    }
}
